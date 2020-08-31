package de.heins.vokabeltraineronline.web.controller;



import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.session.StandardSessionFacade;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.heins.vokabeltraineronline.business.service.LearningStrategyService;
import de.heins.vokabeltraineronline.business.service.SuccessStepService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.LearningStrategyAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.SuccessStepAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.EditOrCreateLearningStrategyModAtt;

@Controller
public class EditOrCreateLearningStrategyController {
	private static enum Constants {
		editOrCreateLearningStrategyPage//
		, sessionEditOrCreateLearningStrategy//
		, editOrCreateLearningStrategyModAtt
	}
	
	@Autowired
	private LearningStrategyService learningStrategyService;
	@Autowired
	private SuccessStepService successStepService;

	public EditOrCreateLearningStrategyController() {
		super();
	}

	@RequestMapping({ "/controlEditOrCreateLearningStrategy" })
	public String showEditOrCreateLearningStrategyPage(//
			StandardSessionFacade session//
			, Model model//
	) throws Exception {
		
		SessionAppUser sessionAppUser = (SessionAppUser) session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		EditOrCreateLearningStrategyModAtt sessionEditOrCreateLearningStrategyModAtt = new EditOrCreateLearningStrategyModAtt();
		String oldVersionOfLearningStrategyName = (String) session.getAttribute(//
				ControllerConstants.sessionOldVersionOfLearningStrategyName.name()//
		);
		if (Strings.isEmpty(oldVersionOfLearningStrategyName)) {
			sessionEditOrCreateLearningStrategyModAtt.setLearningStrategy(new LearningStrategyAttrRef());
		} else {
			sessionEditOrCreateLearningStrategyModAtt.setLearningStrategy(//
					learningStrategyService.findForAppUserAndName(sessionAppUser, oldVersionOfLearningStrategyName)//
			);
		}
		//keine Ahnung, warum die Selectable Steps hier nicht mehr vorhanden sind.
		// Ich füge sie einfach noch mal hinzu...
	    initSelectableSteps(sessionAppUser, sessionEditOrCreateLearningStrategyModAtt);
	    model.addAttribute(Constants.editOrCreateLearningStrategyModAtt.name(), sessionEditOrCreateLearningStrategyModAtt);
	    //The ModelAttribute is stored redundantly as session attribute because in
	    // methods called by http-links it can't be restored from the model.
	    // in each of the methods 
	    // removeSuccessStep
	    // moveUpSuccessStep
	    // moveDownSuccessStep
	    // it is restored from the session.
	    session.setAttribute(//
				Constants.sessionEditOrCreateLearningStrategy.name()//
				, sessionEditOrCreateLearningStrategyModAtt//
		);
		return Constants.editOrCreateLearningStrategyPage.name();
	}

	private void initSelectableSteps(SessionAppUser sessionAppUser,
			EditOrCreateLearningStrategyModAtt sessionEditOrCreateLearningStrategyModAtt) {
		List<SuccessStepAttrRef> selectableSuccessSteps = successStepService.findAllForAppUser(sessionAppUser);
	    sessionEditOrCreateLearningStrategyModAtt.setSelectableSuccessSteps(new ArrayList<String>());
	    selectableSuccessSteps.forEach(//
	    		currentStep -> sessionEditOrCreateLearningStrategyModAtt//
	    			.getSelectableSuccessSteps()//
	    			.add(currentStep.getName())//
	    );
	}
	@RequestMapping(value="/controlActionEditOrCreateLearningStrategy", method=RequestMethod.POST, params= {"submit"})
	public String submit(//
			StandardSessionFacade session//
			, @ModelAttribute("editOrCreateLearningStrategyModAtt")
			EditOrCreateLearningStrategyModAtt editOrCreateLearningStrategyModAtt//
	) {
		editOrCreateLearningStrategyModAtt.setMandatoryViolated(false);
		editOrCreateLearningStrategyModAtt.setLearningStrategyWithThisNameAlreadyExists(false);
		SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		String oldVersionOfLearningStrategyName = (String) session.getAttribute(//
				ControllerConstants.sessionOldVersionOfLearningStrategyName.name()//
		);
		//keine Ahnung, warum die Selectable Steps hier nicht mehr vorhanden sind.
		// Ich füge sie einfach noch mal hinzu...
	    initSelectableSteps(sessionAppUser, editOrCreateLearningStrategyModAtt);
		if (Strings.isEmpty(editOrCreateLearningStrategyModAtt.getLearningStrategy().getName())) {
			editOrCreateLearningStrategyModAtt.setMandatoryViolated(true);
			return Constants.editOrCreateLearningStrategyPage.name();
		}
		if (//
				Strings.isEmpty(oldVersionOfLearningStrategyName)//
				|| !oldVersionOfLearningStrategyName.equals(//
						editOrCreateLearningStrategyModAtt.getLearningStrategy().getName())//
			) {
			// look for duplicates
			LearningStrategyAttrRef fromDataBase = learningStrategyService.findForAppUserAndName(//
					sessionAppUser//
					, editOrCreateLearningStrategyModAtt.getLearningStrategy().getName()//
			);
			if (LearningStrategyService.EMPTY_LEARNING_STRATEGY != fromDataBase) {
				editOrCreateLearningStrategyModAtt.setLearningStrategyWithThisNameAlreadyExists(true);
				return Constants.editOrCreateLearningStrategyPage.name();
			}
		}
		learningStrategyService.update(//
				sessionAppUser//
				, editOrCreateLearningStrategyModAtt.getLearningStrategy()//
				, oldVersionOfLearningStrategyName//
		);
		return "redirect:" + ControllerConstants.controlManageConfigurations.name();
	}
	
	@RequestMapping(value="/controlActionEditOrCreateLearningStrategy", method=RequestMethod.POST, params= {"cancel"})
	public String cancel() {
		return "redirect:" + ControllerConstants.controlManageConfigurations.name();
		
	}
	@RequestMapping({"controlLinkAddSuccessStep"})
	public String addSuccessStep(//
            @RequestParam(name = "name", required = false, defaultValue = "")
            String name//
            , StandardSessionFacade session//
            , Model model//
	) throws Exception {
		EditOrCreateLearningStrategyModAtt editOrCreateLearningStrategyModAtt//
			= (EditOrCreateLearningStrategyModAtt) session.getAttribute(//
					Constants.sessionEditOrCreateLearningStrategy.name()//
			);
		editOrCreateLearningStrategyModAtt//
			.getLearningStrategy()//
			.getAssignedSuccessSteps()//
			.add(name);
	    model.addAttribute(Constants.editOrCreateLearningStrategyModAtt.name(), editOrCreateLearningStrategyModAtt);
		return Constants.editOrCreateLearningStrategyPage.name();
	}
	@RequestMapping({"controlLinkRemoveSuccessStep"})
	public String removeSuccessStep(//
			StandardSessionFacade session//
           ,  @RequestParam(name = "index", required = false, defaultValue = "")
            int index//
           , Model model//
	) throws Exception {
		EditOrCreateLearningStrategyModAtt sessionEditOrCreateLearningStrategyModAtt//
		= (EditOrCreateLearningStrategyModAtt) session.getAttribute(//
				Constants.sessionEditOrCreateLearningStrategy.name()//
		);
		sessionEditOrCreateLearningStrategyModAtt.getLearningStrategy().getAssignedSuccessSteps().remove(index);
	    model.addAttribute(Constants.editOrCreateLearningStrategyModAtt.name(), sessionEditOrCreateLearningStrategyModAtt);
		return Constants.editOrCreateLearningStrategyPage.name();
	}
	@RequestMapping({"controlLinkMoveUpSuccessStep"})
	public String moveUpSuccessStep(//
			StandardSessionFacade session//
	        ,  @RequestParam(name = "index", required = false, defaultValue = "")
            int index//
            , Model model//
	) throws Exception {
		EditOrCreateLearningStrategyModAtt sessionEditOrCreateLearningStrategyModAtt//
		= (EditOrCreateLearningStrategyModAtt) session.getAttribute(//
				Constants.sessionEditOrCreateLearningStrategy.name()//
		);
		if (index>0) {
			String successStepToMoveUp = sessionEditOrCreateLearningStrategyModAtt//
					.getLearningStrategy()//
					.getAssignedSuccessSteps()//
					.remove(index);
			sessionEditOrCreateLearningStrategyModAtt//
					.getLearningStrategy()//
					.getAssignedSuccessSteps()//
					.add(index - 1, successStepToMoveUp);
		}
	    model.addAttribute(Constants.editOrCreateLearningStrategyModAtt.name(), sessionEditOrCreateLearningStrategyModAtt);
		return Constants.editOrCreateLearningStrategyPage.name();
	}
	@RequestMapping({"controlLinkMoveDownSuccessStep"})
	public String moveDownSuccessStep(//
			StandardSessionFacade session//
	        ,  @RequestParam(name = "index", required = false, defaultValue = "")
            int index//
            , Model model//
	) throws Exception {
		EditOrCreateLearningStrategyModAtt sessionEditOrCreateLearningStrategyModdAtt//
		= (EditOrCreateLearningStrategyModAtt) session.getAttribute(//
				Constants.sessionEditOrCreateLearningStrategy.name()//
		);
		List<String> assignedSuccessSteps = sessionEditOrCreateLearningStrategyModdAtt.getLearningStrategy().getAssignedSuccessSteps();
		if (index<assignedSuccessSteps.size()-1) {
			String successStepToMoveUp = assignedSuccessSteps//
					.remove(index);
			assignedSuccessSteps.add(index + 1, successStepToMoveUp);
		}
	    model.addAttribute(Constants.editOrCreateLearningStrategyModAtt.name(), sessionEditOrCreateLearningStrategyModdAtt);
		return Constants.editOrCreateLearningStrategyPage.name();
	}


}
