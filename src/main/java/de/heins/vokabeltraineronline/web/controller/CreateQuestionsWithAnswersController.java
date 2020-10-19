package de.heins.vokabeltraineronline.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.heins.vokabeltraineronline.business.service.IndexBoxService;
import de.heins.vokabeltraineronline.business.service.LearningStrategyService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.LearningStrategyAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.CreateQuestionsWithAnswersModAtt;

@Controller
public class CreateQuestionsWithAnswersController {
	private static enum Constants {
		createQuestionsWithAnswersPage
		, createQuestionsWithAnswersModAtt
	}
	
	@Autowired
	private IndexBoxService indexBoxService;

	@Autowired
	private LearningStrategyService learningStrategyService;
	
	public CreateQuestionsWithAnswersController() {
		super();
	}

	@RequestMapping({ "/controlPageCreateQuestionsWithAnswers" })
	public String showCreateQuestionsWithAnswersPage(//
			StandardSessionFacade session//
			, Model model//
	) throws Exception {
		CreateQuestionsWithAnswersModAtt createQuestionsWithAnswersModAtt = new CreateQuestionsWithAnswersModAtt();
		SessionAppUser sessionAppUser = (SessionAppUser) session.getAttribute(ControllerConstants.sessionAppUser.name());
		List<IndexBoxAttrRef> indexBoxes = indexBoxService.findAllForAppUser(sessionAppUser);
		createQuestionsWithAnswersModAtt.setNoIndexBoxExists(indexBoxes.size() == 0);

		List<String> localIndexBoxDescriptions = indexBoxes.stream().map(
				currentIndexBox -> currentIndexBox.getName() + ", " + currentIndexBox.getSubject()
		).collect(Collectors.toList());
		createQuestionsWithAnswersModAtt.setIndexBoxesDescriptions(
				localIndexBoxDescriptions
		);
		List<LearningStrategyAttrRef> learningStrategies = learningStrategyService.findAllForAppUser(sessionAppUser);
		List<String> localLearningStrategiesDescriptions = learningStrategies.stream().map(
				currentLearningStrategy -> currentLearningStrategy.getName()
		).collect(Collectors.toList());
		createQuestionsWithAnswersModAtt.setLearningStrategiesDescriptions(localLearningStrategiesDescriptions);
		createQuestionsWithAnswersModAtt.setIndexBoxesDescriptions(
				localIndexBoxDescriptions
		);
		
		model.addAttribute(Constants.createQuestionsWithAnswersModAtt.name(), createQuestionsWithAnswersModAtt);
		
		return Constants.createQuestionsWithAnswersPage.name();
	}

	@RequestMapping(value="/controlActionManageQuestionsWithAnswers", method=RequestMethod.POST, params= {"cancel"})
	public String cancel() {
		return "redirect:" + ControllerConstants.controlPageManageQuestionsWithAnswers.name();
		
	}
	@RequestMapping(value="/controlActionManageQuestionsWithAnswers", method=RequestMethod.POST, params= {"next"})
	public String next() {
		return "redirect:" + ControllerConstants.controlPageCreateQuestionsWithAnswers.name();
		
	}
	@RequestMapping(value="/controlActionManageQuestionsWithAnswers", method=RequestMethod.POST, params= {"submit"})
	public String submit() {
		return "redirect:" + ControllerConstants.controlPageManageQuestionsWithAnswers.name();
		
	}

}
