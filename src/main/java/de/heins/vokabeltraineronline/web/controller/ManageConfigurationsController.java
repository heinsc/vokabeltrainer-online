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
import de.heins.vokabeltraineronline.business.service.LearnProfileService;
import de.heins.vokabeltraineronline.business.service.LearningStrategyService;
import de.heins.vokabeltraineronline.business.service.SuccessStepService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.LearnProfileAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.LearningStrategyAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.SuccessStepAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.ManageConfigurationsModAtt;

@Controller
public class ManageConfigurationsController {
	private static enum Constants{
		manageConfigurationsPage, manageConfigurationsModAtt
	}
	@Autowired
	private LearnProfileService learnProfileService;
	
	@Autowired
	private IndexBoxService indexBoxService;

	@Autowired
	private LearningStrategyService learningStrategyService;

	@Autowired
	private SuccessStepService successStepService;

	public ManageConfigurationsController() {
		super();
	}

	@RequestMapping({ "/controlPageManageConfigurations" })
	public String showManageConfigurationsPage(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		ManageConfigurationsModAtt manageConfigurationsModAtt = new ManageConfigurationsModAtt();
		
		LearnProfileAttrRef learnProfile = learnProfileService.findLearnProfileByUser(sessionAppUser);
		manageConfigurationsModAtt.setLearnProfile(learnProfile);
		
	    List<IndexBoxAttrRef> allIndexBoxes = indexBoxService.findAllForAppUser(sessionAppUser);
	    manageConfigurationsModAtt.setAllIndexBoxes(allIndexBoxes);
	    
	    List<LearningStrategyAttrRef> allLearningStrategies = learningStrategyService.findAllForAppUser(sessionAppUser);
	    manageConfigurationsModAtt.setAllLearningStrategies(allLearningStrategies);
		
		List<SuccessStepAttrRef> allSuccessSteps = successStepService.findAllForAppUser(sessionAppUser);
		manageConfigurationsModAtt.setAllSuccessSteps(allSuccessSteps);
		model.addAttribute(Constants.manageConfigurationsModAtt.name(), manageConfigurationsModAtt);
		return Constants.manageConfigurationsPage.name();
	}
	@RequestMapping({"controlLinkDeleteSuccessStep"})
	public String deleteSuccessStep(//
            @RequestParam(name = "name", required = false, defaultValue = "")
            String name//
			, StandardSessionFacade session//
	) throws Exception {
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfSuccessStepName.name()//
	    		, name//
	    );
		return "redirect:" + ControllerConstants.controlPageDeleteSuccessStep.name();
	}
	@RequestMapping({"controlLinkDeleteLearningStrategy"})
	public String deleteLearningStrategy(//
            @RequestParam(name = "name", required = false, defaultValue = "")
            String name//
			, StandardSessionFacade session//
	) throws Exception {
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfLearningStrategyName.name()//
	    		, name//
	    );
		return "redirect:" + ControllerConstants.controlPageDeleteLearningStrategy.name();
	}
	@RequestMapping(value="/controlActionManageConfiguration", method=RequestMethod.POST, params= {"createSuccessStep"})
	public String createSuccessStep(//
			StandardSessionFacade session//
	) throws Exception {
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfSuccessStepName.name()//
	    		, ""//
	    );
	    	    
		return "redirect:" + ControllerConstants.controlPageEditOrCreateSuccessStep.name();
	}
	@RequestMapping(value="/controlActionManageConfiguration", method=RequestMethod.POST, params= {"createLearningStrategy"})
	public String createLearningStrategy(//
			StandardSessionFacade session//
	) throws Exception {
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfLearningStrategyName.name()//
	    		, ""//
	    );
	    	    
		return "redirect:" + ControllerConstants.controlPageEditOrCreateLearningStrategy.name();
	}
	@RequestMapping(value="/controlActionManageConfiguration", method=RequestMethod.POST, params= {"backToMenu"})
	public String backToMenu() {
		//direct go to mainMenu
		return "redirect:" +ControllerConstants.controlPageMenu.name();
	}
	@RequestMapping(value="/controlActionManageConfiguration", method=RequestMethod.POST, params= {"logout"})
	public String logout() {
		//direct go to login 
		return "redirect:" + ControllerConstants.controlPageLogin.name();
	}

	@RequestMapping({"controlLinkEditSuccessStep"})
	public String editSuccessStep(//
	        @RequestParam(name = "name", required = false, defaultValue = "")
	        String name//
			, StandardSessionFacade session//
	) throws Exception {
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfSuccessStepName.name()//
	    		, name//
	    );
		return "redirect:" + ControllerConstants.controlPageEditOrCreateSuccessStep.name();
	}
	@RequestMapping({"controlLinkEditLearningStrategy"})
	public String editLearningStrategy(//
	        @RequestParam(name = "name", required = false, defaultValue = "")
	        String name//
			, StandardSessionFacade session//
	) throws Exception {
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfLearningStrategyName.name()//
	    		, name//
	    );
		return "redirect:" + ControllerConstants.controlPageEditOrCreateLearningStrategy.name();
	}

	@RequestMapping(value="/controlActionManageConfiguration", method=RequestMethod.POST, params= {"createIndexBox"})
	public String createIndexBox(//
			StandardSessionFacade session//
	) throws Exception {
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfIndexBoxName.name()//
	    		, ""//
	    );
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfIndexBoxSubject.name()//
	    		, ""//
	    );
	    	    
		return "redirect:" + ControllerConstants.controlEditOrCreateIndexBox.name();
	}

	@RequestMapping({"controlLinkEditIndexBox"})
	public String editIndexBox(//
	        @RequestParam(name = "name", required = false, defaultValue = "")
	        String name//
	       ,  @RequestParam(name = "subject", required = false, defaultValue = "")
	        String subject//
			, StandardSessionFacade session//
	) throws Exception {
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfIndexBoxName.name()//
	    		, name//
	    );
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfIndexBoxSubject.name()//
	    		, subject//
	    );
		return "redirect:" + ControllerConstants.controlEditOrCreateIndexBox.name();
	}

	@RequestMapping({"controlLinkDeleteIndexBox"})
	public String deleteIndexBox(//
	        @RequestParam(name = "name", required = false, defaultValue = "")
	        String name//
	        , @RequestParam(name = "subject", required = false, defaultValue = "")
	        String subject//
			, StandardSessionFacade session//
	) throws Exception {
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfIndexBoxName.name()//
	    		, name//
	    );
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfIndexBoxSubject.name()//
	    		, subject//
	    );
		return "redirect:" + ControllerConstants.controlDeleteIndexBox.name();
	}

	@RequestMapping(value="/controlActionManageConfiguration", method=RequestMethod.POST, params= {"editLearnProfile"})
	public String editLearnProfile() throws Exception {
		return "redirect:" + ControllerConstants.controlEditLearnProfile.name();
	}


}
