package de.heins.vokabeltraineronline.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.catalina.session.StandardSessionFacade;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.heins.vokabeltraineronline.business.service.IndexBoxService;
import de.heins.vokabeltraineronline.business.service.LearningStrategyService;
import de.heins.vokabeltraineronline.business.service.QuestionWithAnswerService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.LearningStrategyAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.QuestionWithAnswerAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.CreateQuestionsWithAnswersModAtt;

@Controller
public class CreateQuestionsWithAnswersController {
	private static enum Constants {
		createQuestionsWithAnswersPage
		, createQuestionsWithAnswersModAtt
	}
	private static final String OLD_QUESTION_FOR_NEW_QUESTION="";
	
	@Autowired
	private ManageQuestionsWithAnswersController manageQuestionsWithAnswersController;
	@Autowired
	private IndexBoxService indexBoxService;

	@Autowired
	private LearningStrategyService learningStrategyService;
	
	@Autowired
	private QuestionWithAnswerService questionWithAnswerService;
	
	public CreateQuestionsWithAnswersController() {
		super();
	}

	public String showCreateQuestionsWithAnswersPage(//
			Model model//
			, StandardSessionFacade session//
	) {
		CreateQuestionsWithAnswersModAtt createQuestionsWithAnswersModAtt = new CreateQuestionsWithAnswersModAtt();
		SessionAppUser  sessionAppUser = (SessionAppUser) session.getAttribute(ControllerConstants.sessionAppUser.name());
		refreshSelectBoxes(sessionAppUser, createQuestionsWithAnswersModAtt);
		
		model.addAttribute(Constants.createQuestionsWithAnswersModAtt.name(), createQuestionsWithAnswersModAtt);
		
		return Constants.createQuestionsWithAnswersPage.name();
	}

	private void refreshLearningStrategyDescriptions(//
			CreateQuestionsWithAnswersModAtt createQuestionsWithAnswersModAtt//
			, SessionAppUser sessionAppUser//
	) {
		List<LearningStrategyAttrRef> learningStrategies = learningStrategyService.findAllForAppUser(sessionAppUser);
		List<String> localLearningStrategiesDescriptions = learningStrategies.stream().map(
				currentLearningStrategy -> currentLearningStrategy.getName()
		).collect(Collectors.toList());
		createQuestionsWithAnswersModAtt.setLearningStrategiesDescriptions(localLearningStrategiesDescriptions);
		QuestionWithAnswerAttrRef questionWithAnswer = createQuestionsWithAnswersModAtt//
		.getQuestionWithAnswer();
		if (questionWithAnswer != null) {
			createQuestionsWithAnswersModAtt.setSelectedLearningStrategyIndex(//
					localLearningStrategiesDescriptions.indexOf(//
							questionWithAnswer//
									.getLearningStrategyDescription()//
					));
		} else {
			createQuestionsWithAnswersModAtt.setSelectedLearningStrategyIndex(-1);
		}
	}

	private void refreshIndexBoxDescriptions(//
			CreateQuestionsWithAnswersModAtt createQuestionsWithAnswersModAtt//
			, SessionAppUser sessionAppUser//
	) {
		List<IndexBoxAttrRef> indexBoxes = indexBoxService.findAllForAppUser(sessionAppUser);
		createQuestionsWithAnswersModAtt.setNoIndexBoxExists(indexBoxes.size() == 0);
		List<String> localIndexBoxDescriptions = indexBoxes.stream().map(
				currentIndexBox -> 
					// the structure of indexBoxDescription is important for adding or editing 
					// QuestionsWithAnswerService#update
					currentIndexBox.getName()//
					+ QuestionWithAnswerService.INDEXBOX_DESCRIPTION_SPLITTER//
					+ currentIndexBox.getSubject()
		).collect(Collectors.toList());
		createQuestionsWithAnswersModAtt.setIndexBoxesDescriptions(
				localIndexBoxDescriptions
		);
		QuestionWithAnswerAttrRef questionWithAnswer = createQuestionsWithAnswersModAtt//
		.getQuestionWithAnswer();
		if (questionWithAnswer != null) {
			createQuestionsWithAnswersModAtt.setSelectedIndexBoxIndex(//
					localIndexBoxDescriptions.indexOf(//
						questionWithAnswer//
						.getIndexBoxDescription()//
				)//
			);
		} else {
			createQuestionsWithAnswersModAtt.setSelectedIndexBoxIndex(-1);
		}

	}

	@RequestMapping(value="/controlActionCreateQuestionsWithAnswers", method=RequestMethod.POST, params= {"cancel"})
	public String cancel(Model model, StandardSessionFacade session) {
		return manageQuestionsWithAnswersController.showManageQuestionsWithAnswersPage(model, session);
		
	}
	@RequestMapping(value="/controlActionCreateQuestionsWithAnswers", method=RequestMethod.POST, params= {"next"})
	public String next(//
			StandardSessionFacade session//
			, @ModelAttribute(name = "createQuestionsWithAnswersModAtt")
			CreateQuestionsWithAnswersModAtt createQuestionsWithAnswersModAtt
	) {
		String returnValue = trySaveQuestionWithAnswer(session, createQuestionsWithAnswersModAtt);
		if (!Strings.isEmpty(returnValue)) {
			return returnValue;
		}
		createQuestionsWithAnswersModAtt.getQuestionWithAnswer().setQuestion("");
		createQuestionsWithAnswersModAtt.getQuestionWithAnswer().setAnswer("");
		/*
		 * TODO recently added hinzufügen
		 */
		return Constants.createQuestionsWithAnswersPage.name();
	}

	private void refreshSelectBoxes(//
			SessionAppUser sessionAppUser//
			, CreateQuestionsWithAnswersModAtt createQuestionsWithAnswersModAtt//
	) {
		refreshIndexBoxDescriptions(//
				createQuestionsWithAnswersModAtt//
				, sessionAppUser//
		);
		refreshLearningStrategyDescriptions(//
				createQuestionsWithAnswersModAtt//
				, sessionAppUser//
		);
	}
	@RequestMapping(value="/controlActionCreateQuestionsWithAnswers", method=RequestMethod.POST, params= {"submit"})
	public String submit(
			Model model//
			, StandardSessionFacade session//
			, @ModelAttribute(name = "createQuestionsWithAnswersModAtt")
			CreateQuestionsWithAnswersModAtt createQuestionsWithAnswersModAtt
	) {
		String returnValue = trySaveQuestionWithAnswer(session, createQuestionsWithAnswersModAtt);
		if (!Strings.isEmpty(returnValue)) {
			return returnValue;
		}
		return manageQuestionsWithAnswersController.showManageQuestionsWithAnswersPage(model, session);
	}

	private String trySaveQuestionWithAnswer(StandardSessionFacade session,
			CreateQuestionsWithAnswersModAtt createQuestionsWithAnswersModAtt) {
		SessionAppUser sessionAppUser = (SessionAppUser) session.getAttribute(ControllerConstants.sessionAppUser.name());
		createQuestionsWithAnswersModAtt.setMandatoryViolated(false);
		createQuestionsWithAnswersModAtt.setQuestionAlreadyExists(false);
		refreshSelectBoxes(sessionAppUser, createQuestionsWithAnswersModAtt);
		if (Strings.isEmpty(createQuestionsWithAnswersModAtt.getQuestionWithAnswer().getQuestion())) {
			createQuestionsWithAnswersModAtt.setMandatoryViolated(true);
			return Constants.createQuestionsWithAnswersPage.name();
		}
		// in comparison to de.heins.vokabeltraineronline.web.controller.EditOrCreateSuccessStepController.submit(EditOrCreateSuccessStepModAttr, StandardSessionFacade, Model)
		// here the instance is always created. So the if clause for oldQuestion is not needed.
		// the check for duplicates has to be done in every case
		QuestionWithAnswerAttrRef fromDataBase = questionWithAnswerService.findForAppUserAndQuestion(//
				sessionAppUser//
				, createQuestionsWithAnswersModAtt.getQuestionWithAnswer().getQuestion()
		);
		if (QuestionWithAnswerService.EMPTY_QUESTION_WITH_ANSWER != fromDataBase) {
			createQuestionsWithAnswersModAtt.setQuestionAlreadyExists(true);
			return Constants.createQuestionsWithAnswersPage.name();
		}
		questionWithAnswerService.update(//
				sessionAppUser//
				, createQuestionsWithAnswersModAtt.getQuestionWithAnswer()//
				, OLD_QUESTION_FOR_NEW_QUESTION//
		);
		return "";
	}

}
