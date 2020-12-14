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
		editOrCreateLearningStrategyNamePage//
		, sessionEditOrCreateLearningStrategy//
		, editOrCreateLearningStrategyModAtt//
		, editOrCreateLearningStrategySuccessStepsPage//
		, sessionOldVersionOfLearningStrategyName
	}
	@Autowired
	private ManageConfigurationsController manageConfigurationsController;
	@Autowired
	private LearningStrategyService learningStrategyService;
	@Autowired
	private SuccessStepService successStepService;

	public EditOrCreateLearningStrategyController() {
		super();
	}

	public String showEditOrCreateLearningStrategyPages(//
			String oldVersionOfLearningStrategyName//
			, StandardSessionFacade session//
			, Model model//
	) {
		SessionAppUser sessionAppUser = (SessionAppUser) session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		EditOrCreateLearningStrategyModAtt editOrCreateLearningStrategyModAtt = new EditOrCreateLearningStrategyModAtt();
		if (Strings.isEmpty(oldVersionOfLearningStrategyName)) {
			LearningStrategyAttrRef learningStrategy = new LearningStrategyAttrRef();
			editOrCreateLearningStrategyModAtt.setLearningStrategy(learningStrategy);
		} else {
			editOrCreateLearningStrategyModAtt.setLearningStrategy(//
					learningStrategyService.findForAppUserAndName(sessionAppUser, oldVersionOfLearningStrategyName)//
			);
		}
		model.addAttribute(//
				Constants.editOrCreateLearningStrategyModAtt.name()//
				, editOrCreateLearningStrategyModAtt//
		);
		session.setAttribute(//
				Constants.sessionEditOrCreateLearningStrategy.name()//
				, editOrCreateLearningStrategyModAtt//
		);
		session.setAttribute(//
				Constants.sessionOldVersionOfLearningStrategyName.name()//
				, editOrCreateLearningStrategyModAtt.getLearningStrategy().getName()//
		);
				
		return Constants.editOrCreateLearningStrategyNamePage.name();
	}

	private void initSelectableSteps(//
			SessionAppUser sessionAppUser//
			, EditOrCreateLearningStrategyModAtt sessionEditOrCreateLearningStrategyModAtt//
	) {
		List<String> assignedSuccessSteps = sessionEditOrCreateLearningStrategyModAtt.getLearningStrategy().getAssignedSuccessSteps();
		List<SuccessStepAttrRef> selectableSuccessSteps = successStepService.findAllForAppUser(sessionAppUser);
	    sessionEditOrCreateLearningStrategyModAtt.setSelectableSuccessSteps(new ArrayList<String>());
	    selectableSuccessSteps.forEach(//
	    		currentStep -> {//
	    			String successStepName = currentStep.getName();
	    			if (!assignedSuccessSteps.contains(successStepName)) {//
						sessionEditOrCreateLearningStrategyModAtt//
		    			.getSelectableSuccessSteps()//
		    			.add(successStepName);//
	    			}
	    		}
	    );
	}
	@RequestMapping(value="/controlActionEditOrCreateLearningStrategy", method=RequestMethod.POST, params= {"submit"})
	public String submit(//
			Model model//
			, StandardSessionFacade session//
	) {
		SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		String oldVersionOfLearningStrategyName = (String) session.getAttribute(//
				Constants.sessionOldVersionOfLearningStrategyName.name()//
		);
		// if you miss the check for duplicates here (like in de.heins.vokabeltraineronline.web.controller.EditOrCreateSuccessStepController.submit(EditOrCreateSuccessStepModAttr, StandardSessionFacade, Model)
		// the check for duplicates already happens in 
		// de.heins.vokabeltraineronline.web.controller.EditOrCreateLearningStrategyController.showAssignSuccessStepsPage(StandardSessionFacade, Model, EditOrCreateLearningStrategyModAtt)
		EditOrCreateLearningStrategyModAtt editOrCreateLearningStrategyModAtt//
			= (EditOrCreateLearningStrategyModAtt) session.getAttribute(Constants.sessionEditOrCreateLearningStrategy.name()//
		);
		learningStrategyService.update(//
				sessionAppUser//
				, editOrCreateLearningStrategyModAtt.getLearningStrategy()//
				, oldVersionOfLearningStrategyName 
		);
		return backToManageConfigurations(model, session);
	}
	@RequestMapping(value="/controlActionEditOrCreateLearningStrategy", method=RequestMethod.POST, params= {"assignSuccessSteps"})
	public String goToAssignSuccessStepsPage(//
			StandardSessionFacade session//
			, Model model
			, @ModelAttribute(name = "editOrCreateLearningStrategyModAtt" )
			EditOrCreateLearningStrategyModAtt editOrCreateLearningStrategyModAtt
	) {
		editOrCreateLearningStrategyModAtt.setMandatoryViolated(false);
		editOrCreateLearningStrategyModAtt.setLearningStrategyWithThisNameAlreadyExists(false);
		SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		String oldVersionOfLearningStrategyName = (String) session.getAttribute(//
				Constants.sessionOldVersionOfLearningStrategyName.name()//
		);
		model.addAttribute(Constants.editOrCreateLearningStrategyModAtt.name(), editOrCreateLearningStrategyModAtt);
		if (Strings.isEmpty(editOrCreateLearningStrategyModAtt.getLearningStrategy().getName())) {
			editOrCreateLearningStrategyModAtt.setMandatoryViolated(true);
			return Constants.editOrCreateLearningStrategyNamePage.name();
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
				return Constants.editOrCreateLearningStrategyNamePage.name();
			}
		}
		
		EditOrCreateLearningStrategyModAtt sessionEditOrCreateLearningStrategyModAtt
			= (EditOrCreateLearningStrategyModAtt) session.getAttribute(//
					Constants.sessionEditOrCreateLearningStrategy.name()//
			);
		sessionEditOrCreateLearningStrategyModAtt.getLearningStrategy().setName(//
				editOrCreateLearningStrategyModAtt.getLearningStrategy().getName()//
		);
		editOrCreateLearningStrategyModAtt.getLearningStrategy().getAssignedSuccessSteps().clear();
		editOrCreateLearningStrategyModAtt.getLearningStrategy().getAssignedSuccessSteps().addAll(//
				sessionEditOrCreateLearningStrategyModAtt.getLearningStrategy().getAssignedSuccessSteps()//
		);
	    initSelectableSteps(sessionAppUser, editOrCreateLearningStrategyModAtt);
	    // the model attribute must be stored redundantly as session attribute
	    // because on hyperlink calls there is no access on the model attributes. 
	    // Then, all changes on the model a lost.
//	    session.setAttribute(//
//	    		Constants.sessionEditOrCreateLearningStrategy.name()//
//	    		, editOrCreateLearningStrategyModAtt//
//	    );
		return Constants.editOrCreateLearningStrategySuccessStepsPage.name();
	}
	@RequestMapping(value="/controlActionEditOrCreateLearningStrategy", method=RequestMethod.POST, params= {"editName"})
	public String goToNamePage(//
			StandardSessionFacade session//
			, Model model
	) {
		
		EditOrCreateLearningStrategyModAtt sessionEditOrCreateLearningStrategyModAtt//
		= (EditOrCreateLearningStrategyModAtt) session.getAttribute(//
				Constants.sessionEditOrCreateLearningStrategy.name()//
		);
		sessionEditOrCreateLearningStrategyModAtt.setMandatoryViolated(false);
		sessionEditOrCreateLearningStrategyModAtt.setLearningStrategyWithThisNameAlreadyExists(false);
		
		model.addAttribute(Constants.editOrCreateLearningStrategyModAtt.name(), sessionEditOrCreateLearningStrategyModAtt);
		return Constants.editOrCreateLearningStrategyNamePage.name();
	}

	
	@RequestMapping(value="/controlActionEditOrCreateLearningStrategy", method=RequestMethod.POST, params= {"cancel"})
	public String cancel(Model model, StandardSessionFacade session) {
		return backToManageConfigurations(model, session);
	}

	private String backToManageConfigurations(Model model, StandardSessionFacade session) {
		session.removeAttribute(Constants.sessionOldVersionOfLearningStrategyName.name());
		session.removeAttribute(Constants.sessionEditOrCreateLearningStrategy.name());
		return manageConfigurationsController.showManageConfigurationsPage(model, session);
	}
	@RequestMapping({"controlLinkAddSuccessStep"})
	public String addSuccessStep(//
            @RequestParam(name = "successStepName", required = false, defaultValue = "")
            String successStepName//
            , StandardSessionFacade session//
            , Model model//
	) {
		EditOrCreateLearningStrategyModAtt editOrCreateLearningStrategyModAtt//
			= (EditOrCreateLearningStrategyModAtt) session.getAttribute(//
					Constants.sessionEditOrCreateLearningStrategy.name()//
			);
		editOrCreateLearningStrategyModAtt//
			.getLearningStrategy()//
			.getAssignedSuccessSteps()//
			.add(successStepName);
		SessionAppUser sessionAppUser = (SessionAppUser) session.getAttribute(ControllerConstants.sessionAppUser.name());
		initSelectableSteps(sessionAppUser , editOrCreateLearningStrategyModAtt);
		model.addAttribute(//
	    		Constants.editOrCreateLearningStrategyModAtt.name()//
	    		, editOrCreateLearningStrategyModAtt//
	    );
		return Constants.editOrCreateLearningStrategySuccessStepsPage.name();
	}
	@RequestMapping({"controlLinkRemoveSuccessStep"})
	public String removeSuccessStep(//
			StandardSessionFacade session//
           ,  @RequestParam(name = "index", required = false, defaultValue = "")
            int index//
           , Model model//
	) {
		EditOrCreateLearningStrategyModAtt sessionEditOrCreateLearningStrategyModAtt//
		= (EditOrCreateLearningStrategyModAtt) session.getAttribute(//
				Constants.sessionEditOrCreateLearningStrategy.name()//
		);
		sessionEditOrCreateLearningStrategyModAtt.getLearningStrategy().getAssignedSuccessSteps().remove(index);
	    model.addAttribute(Constants.editOrCreateLearningStrategyModAtt.name(), sessionEditOrCreateLearningStrategyModAtt);
		return Constants.editOrCreateLearningStrategySuccessStepsPage.name();
	}
	@RequestMapping({"controlLinkMoveUpSuccessStep"})
	public String moveUpSuccessStep(//
			StandardSessionFacade session//
	        ,  @RequestParam(name = "index", required = false, defaultValue = "")
            int index//
            , Model model//
	) {
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
		return Constants.editOrCreateLearningStrategySuccessStepsPage.name();
	}
	@RequestMapping({"controlLinkMoveDownSuccessStep"})
	public String moveDownSuccessStep(//
			StandardSessionFacade session//
	        ,  @RequestParam(name = "index", required = false, defaultValue = "")
            int index//
            , Model model//
	) {
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
		return Constants.editOrCreateLearningStrategySuccessStepsPage.name();
	}

}
