package de.heins.vokabeltraineronline.web.controller;


import java.util.List;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.heins.vokabeltraineronline.business.service.IndexBoxService;
import de.heins.vokabeltraineronline.business.service.LearningStrategyService;
import de.heins.vokabeltraineronline.business.service.SuccessStepService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.LearningStrategyModAttr;
import de.heins.vokabeltraineronline.web.entities.attributereference.SuccessStepAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.ManageConfigurationsModAtt;

@Controller
public class ManageConfigurationsController {
	private static enum Constants{
		manageConfigurationsPage, manageConfigurationsModAtt
	}
	
	@Autowired
	private IndexBoxService indexBoxService;

	@Autowired
	private LearningStrategyService learningStrategyService;

	@Autowired
	private SuccessStepService successStepService;

	public ManageConfigurationsController() {
		super();
	}

	@RequestMapping({ "/controlManageConfigurations" })
	public String showManageConfigurationsPage(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		SessionAppUser sessionAppUserForm = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		ManageConfigurationsModAtt manageConfigurationsModAtt = new ManageConfigurationsModAtt();
		model.addAttribute(Constants.manageConfigurationsModAtt.name(), manageConfigurationsModAtt);

	    List<IndexBoxAttrRef> allIndexBoxes = indexBoxService.findAllForAppUser(sessionAppUserForm);
	    // only for testing, remove later
	    {
		    IndexBoxAttrRef indexBoxForm = new IndexBoxAttrRef();
		    indexBoxForm.setName("firstIndexBox");
		    allIndexBoxes.add(indexBoxForm);
		    indexBoxForm = new IndexBoxAttrRef();
		    indexBoxForm.setName("secondIndexBox");
		    allIndexBoxes.add(indexBoxForm);
	    }
	    manageConfigurationsModAtt.setAllIndexBoxes(allIndexBoxes);
	    
	    List<LearningStrategyModAttr> allLearningStrategies = learningStrategyService.findAllForAppUser(sessionAppUserForm);
	    {
	    	// only for testing, remove later
		    LearningStrategyModAttr learningStrategyForm = new LearningStrategyModAttr();
		    learningStrategyForm.setName("firstLearningStrategy");
		    allLearningStrategies.add(learningStrategyForm);
		    learningStrategyForm = new LearningStrategyModAttr();
		    learningStrategyForm.setName("secondLearningStrategy");
		    allLearningStrategies.add(learningStrategyForm);
	    }
	    manageConfigurationsModAtt.setAllLearningStrategies(allLearningStrategies);
		
		List<SuccessStepAttrRef> allSuccessSteps = successStepService.findAllForAppUser(sessionAppUserForm);
		manageConfigurationsModAtt.setAllSuccessSteps(allSuccessSteps);
		return Constants.manageConfigurationsPage.name();
	}
	@RequestMapping(value="/controlActionManageConfiguration", method=RequestMethod.POST, params= {"editSuccessStep"})
	public String editSuccessStep(//
			@ModelAttribute(name="manageConfigurationsModAtt")
			ManageConfigurationsModAtt manageConfigurationsModAtt
			, StandardSessionFacade session//
	) throws Exception {
		List<SuccessStepAttrRef> successSteps = manageConfigurationsModAtt.getAllSuccessSteps();
		int counterSelected = 0;
		SuccessStepAttrRef selectedSuccessStepAttrRef = null;
		for (SuccessStepAttrRef currentSuccessStep : successSteps) {
			if (currentSuccessStep.getSelected()) {
				counterSelected ++;
				selectedSuccessStepAttrRef = currentSuccessStep;
			}
		}
		if (counterSelected != 1) {
			// TODO handle wrong number of selected SuccessSteps
			return Constants.manageConfigurationsPage.name();
		}
		
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfSuccessStepName.name()//
	    		, selectedSuccessStepAttrRef.getName()//
	    );
	    	    
		return "redirect:" + ControllerConstants.controlEditOrCreateSuccessStep.name();
	}
	@RequestMapping(value="/controlActionManageConfiguration", method=RequestMethod.POST, params= {"createSuccessStep"})
	public String createSuccessStep(//
			StandardSessionFacade session//
	) throws Exception {
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfSuccessStepName.name()//
	    		, ""//
	    );
	    	    
		return "redirect:" + ControllerConstants.controlEditOrCreateSuccessStep.name();
	}
	@RequestMapping(value="/controlActionManageConfiguration", method=RequestMethod.POST, params= {"backToMenu"})
	public String backToMenu() {
		//direct go to mainMenu
		return "redirect:" +ControllerConstants.controlMenu.name();
	}
	@RequestMapping(value="/controlActionManageConfiguration", method=RequestMethod.POST, params= {"logout"})
	public String logout() {
		//direct go to login 
		return "redirect:" + ControllerConstants.controlLogin.name();
	}


}
