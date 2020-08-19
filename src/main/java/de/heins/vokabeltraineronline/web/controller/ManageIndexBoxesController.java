package de.heins.vokabeltraineronline.web.controller;


import java.util.List;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import de.heins.vokabeltraineronline.business.entity.SuccessStep;
import de.heins.vokabeltraineronline.business.service.IndexBoxService;
import de.heins.vokabeltraineronline.business.service.LearningStrategyService;
import de.heins.vokabeltraineronline.business.service.SuccessStepService;
import de.heins.vokabeltraineronline.web.entities.IndexBoxForm;
import de.heins.vokabeltraineronline.web.entities.LearningStrategyForm;
import de.heins.vokabeltraineronline.web.entities.SessionAppUserForm;
import de.heins.vokabeltraineronline.web.entities.SuccessStepForm;

@Controller
public class ManageIndexBoxesController {
	
	@Autowired
	private IndexBoxService indexBoxService;

	@Autowired
	private LearningStrategyService learningStrategyService;

	@Autowired
	private SuccessStepService successStepService;

	public ManageIndexBoxesController() {
		super();
	}

	@RequestMapping({ "/manageIndexBoxes" })
	public String manageIndexBoxes(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		SessionAppUserForm sessionAppUserForm = (SessionAppUserForm)session.getAttribute("sessionAppUser");

	    List<IndexBoxForm> allIndexBoxes = indexBoxService.findAllForAppUser(sessionAppUserForm);
	    // only for testing, remove later
	    {
		    IndexBoxForm indexBoxForm = new IndexBoxForm();
		    indexBoxForm.setName("firstIndexBox");
		    allIndexBoxes.add(indexBoxForm);
		    indexBoxForm = new IndexBoxForm();
		    indexBoxForm.setName("secondIndexBox");
		    allIndexBoxes.add(indexBoxForm);
	    }
	    model.addAttribute("indexBoxes", allIndexBoxes);
	    
	    List<LearningStrategyForm> allLearningStrategies = learningStrategyService.findAllForAppUser(sessionAppUserForm);
	    {
	    	// only for testing, remove later
		    LearningStrategyForm learningStrategyForm = new LearningStrategyForm();
		    learningStrategyForm.setName("firstLearningStrategy");
		    allLearningStrategies.add(learningStrategyForm);
		    learningStrategyForm = new LearningStrategyForm();
		    learningStrategyForm.setName("secondLearningStrategy");
		    allLearningStrategies.add(learningStrategyForm);
	    }
	    model.addAttribute("learningStrategies", allLearningStrategies);
		
		List<SuccessStepForm> allSuccessSteps = successStepService.findAllForAppUser(sessionAppUserForm);
		{
	    	// only for testing, remove later
		    SuccessStepForm successStepForm = new SuccessStepForm();
		    successStepForm.setName("firstSuccessStep");
		    successStepForm.setNextAppearanceInDays(5);
		    successStepForm.setBehaviourIfWrong("firstBehaviourIfWrong");
		    allSuccessSteps.add(successStepForm);
		    successStepForm = new SuccessStepForm();
		    successStepForm.setName("secondSuccessStep");
		    successStepForm.setNextAppearanceInDays(10);
		    successStepForm.setBehaviourIfWrong("secondBehaviourIfWrong");
		    allSuccessSteps.add(successStepForm);
	    }
	    model.addAttribute("successSteps", allSuccessSteps);
		return "manageIndexBoxes";

	}

}
