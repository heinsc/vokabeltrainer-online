package de.heins.vokabeltraineronline.web.controller;



import org.apache.catalina.session.StandardSessionFacade;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.heins.vokabeltraineronline.business.service.SuccessStepService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.SuccessStepAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.EditOrCreateSuccessStepModAttr;

@Controller
public class EditOrCreateSuccessStepController {
	private static enum Constants {
		editOrCreateSuccessStepPage//
		, editOrCreateSuccessStepModAtt//
		, sessionOldVersionOfSuccessStepName
	}
	@Autowired
	private ManageConfigurationsController manageConfigurationsController;
	@Autowired
	private SuccessStepService successStepService;

	public EditOrCreateSuccessStepController() {
		super();
	}

	public String showEditOrCreateSuccessStepPage(//
			String oldVersionOfSuccessStepName//
			, Model model//
			, StandardSessionFacade session//
	)  {
		EditOrCreateSuccessStepModAttr editOrCreateSuccessStep = new EditOrCreateSuccessStepModAttr();
		model.addAttribute(//
				Constants.editOrCreateSuccessStepModAtt.name()//
				, editOrCreateSuccessStep);
		
		session.setAttribute(//
				Constants.sessionOldVersionOfSuccessStepName.name()//
				, oldVersionOfSuccessStepName
		);
		SuccessStepAttrRef successStep = null;
		if (Strings.isEmpty(oldVersionOfSuccessStepName)) {
			successStep = new SuccessStepAttrRef();
		} else {
			successStep = successStepService.findForAppUserAndName(//
					(SessionAppUser) session.getAttribute(//
							ControllerConstants.sessionAppUser.name()//
					)//
					, oldVersionOfSuccessStepName//
			);
		}
	    editOrCreateSuccessStep.setSuccessStep(successStep);
	    editOrCreateSuccessStep.setSelectableBehaviours(successStepService.getAllBehavioursIfWrongAsStringArray());
	    editOrCreateSuccessStep.setSelectableFaultTolerances(successStepService.getAllFaultTolerancesAsStringArray());
		return Constants.editOrCreateSuccessStepPage.name();
	}
	@RequestMapping(value="/controlActionEditOrCreateSuccessStep", method=RequestMethod.POST, params= {"submit"})
	public String submit(//
			@ModelAttribute(name = "editOrCreateSuccessStepModAtt")
			EditOrCreateSuccessStepModAttr editOrCreateSuccessModAtt//
			, StandardSessionFacade session//
			, Model model//
	) {
		SessionAppUser sessionAppUserForm = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		editOrCreateSuccessModAtt.setMandatoryViolated(false);
		editOrCreateSuccessModAtt.setSuccessStepWithThisNameAlreadyExists(false);
		//keine Ahnung, warum die selectableBehaviours und die 
		// selectableFaultTolerances weg sind... Ich füge sie an dieser Stelle einfach wieder ein.
		editOrCreateSuccessModAtt.setSelectableBehaviours(successStepService.getAllBehavioursIfWrongAsStringArray());
		editOrCreateSuccessModAtt.setSelectableFaultTolerances(successStepService.getAllFaultTolerancesAsStringArray());
		if (Strings.isEmpty(editOrCreateSuccessModAtt.getSuccessStep().getName())) {
			editOrCreateSuccessModAtt.setMandatoryViolated(true);
			return Constants.editOrCreateSuccessStepPage.name();
		}
		String oldVersionOfSuccessStepName = (String) session.getAttribute(//
				Constants.sessionOldVersionOfSuccessStepName.name()//
		);
		if (//
				Strings.isEmpty(oldVersionOfSuccessStepName)//
				|| !oldVersionOfSuccessStepName.equals(//
						editOrCreateSuccessModAtt.getSuccessStep().getName())//
			) {
			// look for duplicates
			SuccessStepAttrRef fromDataBase = successStepService.findForAppUserAndName(//
					sessionAppUserForm//
					, editOrCreateSuccessModAtt.getSuccessStep().getName()//
			);
			if (SuccessStepService.EMPTY_SUCCESS_STEP != fromDataBase) {
				editOrCreateSuccessModAtt.setSuccessStepWithThisNameAlreadyExists(true);
				return Constants.editOrCreateSuccessStepPage.name();
			}
		}
		successStepService.update(//
				sessionAppUserForm//
				, editOrCreateSuccessModAtt.getSuccessStep()//
				, oldVersionOfSuccessStepName//
		);
		return backToManageConfigurationsPage(session, model);
	}

	private String backToManageConfigurationsPage(StandardSessionFacade session, Model model) {
		session.removeAttribute(Constants.sessionOldVersionOfSuccessStepName.name());
		return manageConfigurationsController.showManageConfigurationsPage(model, session);
	}
	
	@RequestMapping(value="/controlActionEditOrCreateSuccessStep", method=RequestMethod.POST, params= {"cancel"})
	public String cancel(Model model, StandardSessionFacade session) {
		return backToManageConfigurationsPage(session, model);
		
	}

}
