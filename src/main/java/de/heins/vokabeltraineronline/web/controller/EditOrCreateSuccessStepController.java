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
		, editOrCreateSuccessStepModAtt
	}
	
	@Autowired
	private SuccessStepService successStepService;

	public EditOrCreateSuccessStepController() {
		super();
	}

	@RequestMapping({ "/controlEditOrCreateSuccessStep" })
	public String showEditOrCreateSuccessStepPage(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		
		EditOrCreateSuccessStepModAttr editOrCreateSuccessStep = new EditOrCreateSuccessStepModAttr();
		model.addAttribute(//
				Constants.editOrCreateSuccessStepModAtt.name()//
				, editOrCreateSuccessStep);
		
		String oldVersionOfSuccessStepName = (String) session.getAttribute(//
				ControllerConstants.sessionOldVersionOfSuccessStepName.name()//
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
		return Constants.editOrCreateSuccessStepPage.name();
	}
	@RequestMapping(value="/controlActionEditOrCreateSuccessStep", method=RequestMethod.POST, params= {"submit"})
	public String submit(//
			@ModelAttribute(name = "editOrCreateSuccessStepModAtt")
			EditOrCreateSuccessStepModAttr editOrCreateSuccessModAtt//
			, StandardSessionFacade session//
	) {
		SessionAppUser sessionAppUserForm = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		String oldVersionOfSuccessStepName = (String) session.getAttribute(//
				ControllerConstants.sessionOldVersionOfSuccessStepName.name()//
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
				// handle duplicate new success step
				return Constants.editOrCreateSuccessStepPage.name();
			}
		}
		successStepService.update(//
				sessionAppUserForm, editOrCreateSuccessModAtt.getSuccessStep(), oldVersionOfSuccessStepName);
		return "redirect:" + ControllerConstants.controlManageConfigurations.name();
	}
	
	@RequestMapping(value="/controlActionEditOrCreateSuccessStep", method=RequestMethod.POST, params= {"cancel"})
	public String cancel() {
		return "redirect:" + ControllerConstants.controlManageConfigurations.name();
		
	}

}
