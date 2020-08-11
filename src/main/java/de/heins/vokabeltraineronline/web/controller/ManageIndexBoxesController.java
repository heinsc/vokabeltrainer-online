package de.heins.vokabeltraineronline.web.controller;


import java.util.List;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import de.heins.vokabeltraineronline.business.service.IndexBoxService;
import de.heins.vokabeltraineronline.business.service.LearningStrategyService;
import de.heins.vokabeltraineronline.web.entities.IndexBoxForm;
import de.heins.vokabeltraineronline.web.entities.LearningStrategyForm;
import de.heins.vokabeltraineronline.web.entities.SessionUserForm;

@Controller
public class ManageIndexBoxesController {
	
	@Autowired
	private IndexBoxService indexBoxService;

	@Autowired
	private LearningStrategyService learningStrategyService;

	public ManageIndexBoxesController() {
		super();
	}

	@RequestMapping({ "/manageIndexBoxes" })
	public String manageIndexBoxes(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		SessionUserForm sessionUserForm = (SessionUserForm)session.getAttribute("sessionUser");

	    List<IndexBoxForm> allIndexBoxes = indexBoxService.findAllForUser(sessionUserForm);
	    // only for testing, remove later
	    {
		    LearningStrategyForm learningStrategyForm = new LearningStrategyForm();
		    learningStrategyForm.setName("firstLearningStrategy");
		    IndexBoxForm indexBoxForm = new IndexBoxForm();
		    indexBoxForm.setName("firstIndexBox");
		    indexBoxForm.setLearningStrategyForm(learningStrategyForm);
		    allIndexBoxes.add(indexBoxForm);
		    learningStrategyForm = new LearningStrategyForm();
		    learningStrategyForm.setName("secondLearningStrategy");
		    indexBoxForm = new IndexBoxForm();
		    indexBoxForm.setName("secondIndexBox");
		    indexBoxForm.setLearningStrategyForm(learningStrategyForm);
		    allIndexBoxes.add(indexBoxForm);
	    }
	    model.addAttribute("indexBoxes", allIndexBoxes);
	    
	    List<LearningStrategyForm> allLearningStrategies = learningStrategyService.findAllForUser(sessionUserForm);
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
		return "manageIndexBoxes";

	}

}
