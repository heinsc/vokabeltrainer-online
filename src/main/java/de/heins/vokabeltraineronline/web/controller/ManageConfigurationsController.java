package de.heins.vokabeltraineronline.web.controller;


import java.util.List;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

		manageConfigurationsModAtt.setTestOutput("bla");
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
		model.addAttribute(Constants.manageConfigurationsModAtt.name(), manageConfigurationsModAtt);
		return Constants.manageConfigurationsPage.name();
	}
	@RequestMapping({"controlLinkEditAppUser"})
	public String editSuccessStep(//
            @RequestParam(name = "name", required = false, defaultValue = "")
            String name//
			, StandardSessionFacade session//
	) throws Exception {
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfSuccessStepName.name()//
	    		, name//
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
