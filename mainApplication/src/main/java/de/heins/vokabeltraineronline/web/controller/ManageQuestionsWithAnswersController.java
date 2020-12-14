package de.heins.vokabeltraineronline.web.controller;


import java.util.Set;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.heins.vokabeltraineronline.business.service.IndexBoxService;
import de.heins.vokabeltraineronline.web.entities.IndexBoxes;
import de.heins.vokabeltraineronline.web.entities.QuestionsWithAnswers;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.QuestionWithAnswerAttrRef;

@Controller
public class ManageQuestionsWithAnswersController {
	private static enum Constants{
		sessionQuestionsWithAnswers
		, sessionIndexBoxAttrRefList
		, manageQuestionsWithAnswersPage
	}
	@Autowired
	private MenuController menuController;
	@Autowired
	private ManageQuestionsWithAnswersController manageQuestionsWithAnswersController;
	@Autowired
	private EditQuestionWithAnswerController editQuestionWithAnswerController;
	@Autowired
	private CreateQuestionsWithAnswersController createQuestionsWithAnswersController;
	// TODO
//	@Autowired
//	private EditQuestionWithAnswerController deleteQuestionWithAnswerController
//	@Autowired
//	private DeleteQuestionWithAnserController deleteQuestionWithAnswerController
	@Autowired
	private IndexBoxService indexBoxService;

	public ManageQuestionsWithAnswersController() {
		super();
	}

	public String showManageQuestionsWithAnswersPage(//
			Model model//
			, StandardSessionFacade session//
	) {
		IndexBoxes indexBoxes = refreshIndexBoxes(session);
		QuestionsWithAnswers questionsWithAnswersList = findQuestionsWithAnswersList(indexBoxes);
		session.setAttribute(Constants.sessionQuestionsWithAnswers.name(), questionsWithAnswersList);
		return Constants.manageQuestionsWithAnswersPage.name();
	}

	private IndexBoxes refreshIndexBoxes(//
			StandardSessionFacade session//
	) {
		SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		IndexBoxes indexBoxes = indexBoxService.findAllForAppUser(sessionAppUser);
		session.setAttribute(Constants.sessionIndexBoxAttrRefList.name(), indexBoxes);
		return indexBoxes;
	}

	private QuestionsWithAnswers findQuestionsWithAnswersList(IndexBoxes indexBoxes) {
		QuestionsWithAnswers allQuestionsWithAnswers = new QuestionsWithAnswers();
		for (IndexBoxAttrRef indexBoxAttrRef : indexBoxes) {
			Set<QuestionWithAnswerAttrRef> questionsWithAnswers = indexBoxAttrRef.getQuestionsWithAnswers();
			for (QuestionWithAnswerAttrRef questionWithAnswerAttrRef : questionsWithAnswers) {
				questionWithAnswerAttrRef.setFilterOn(indexBoxAttrRef.isFilterOn());
				if (indexBoxAttrRef.isFilterOn()) {
					allQuestionsWithAnswers.add(questionWithAnswerAttrRef);
				}
			}
		}
		return allQuestionsWithAnswers;
	}
	@RequestMapping({"controlLinkDeleteQuestionWithAnswer"})
	public String deleteQuestionWithAnswer(//
            @RequestParam(name = "question", required = false, defaultValue = "")
            String question//
            , Model model//
			, StandardSessionFacade session//
	) {
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfQuestion.name()//
	    		, question//
	    );
	    return manageQuestionsWithAnswersController.showManageQuestionsWithAnswersPage(model, session);
//		TODO return "redirect:" + ControllerConstants.controlDeleteQuestion.name();
	}
	@RequestMapping({"controlLinkAddIndexBoxToFilter"})
	public String addIndexBoxToFilter(//
			@RequestParam(name = "index", required = false, defaultValue = "")
            int index//
			, StandardSessionFacade session//
	) {
		IndexBoxes indexBoxAttrRefList = (IndexBoxes) session.getAttribute(Constants.sessionIndexBoxAttrRefList.name());
		IndexBoxAttrRef indexBox = indexBoxAttrRefList.get(index);
		indexBox.setFilterOn(true);
		indexBoxService.update(//
				(SessionAppUser) session.getAttribute(ControllerConstants.sessionAppUser.name())//
				, indexBox//
				, indexBox.getName()//
				, indexBox.getSubject()//
		);
		QuestionsWithAnswers questionsWithAnswers = findQuestionsWithAnswersList(indexBoxAttrRefList);
		//noch mal gucken, ob das wirklich nötig ist.
		session.setAttribute(Constants.sessionIndexBoxAttrRefList.name(), indexBoxAttrRefList);
		session.setAttribute(Constants.sessionQuestionsWithAnswers.name(), questionsWithAnswers);
		
		return Constants.manageQuestionsWithAnswersPage.name();
	}
	@RequestMapping({"controlLinkRemoveQuestionWithAnswerFromFilter"})
	public String removeQuestionWithAnswerFromFilter(//
			@RequestParam(name = "index", required = false, defaultValue = "")
            int index//
			, StandardSessionFacade session//
	) {
		QuestionsWithAnswers questionsWithAnswers = (QuestionsWithAnswers) session.getAttribute(Constants.sessionQuestionsWithAnswers.name());
		QuestionWithAnswerAttrRef selectedQuestionWithAnswer = questionsWithAnswers.get(index);
		selectedQuestionWithAnswer.setFilterOn(false);
		return Constants.manageQuestionsWithAnswersPage.name();
	}
	@RequestMapping({"controlLinkAddQuestionWithAnswerToFilter"})
	public String controlLinkAddQuestionWithAnswerToFilter(//
			@RequestParam(name = "index", required = false, defaultValue = "")
            int index//
			, StandardSessionFacade session//
	) {
		QuestionsWithAnswers questionsWithAnswers = (QuestionsWithAnswers) session.getAttribute(Constants.sessionQuestionsWithAnswers.name());
		QuestionWithAnswerAttrRef selectedQuestionWithAnswer = questionsWithAnswers.get(index);
		selectedQuestionWithAnswer.setFilterOn(true);
		return Constants.manageQuestionsWithAnswersPage.name();
	}
	@RequestMapping({"controlLinkRemoveIndexBoxFromFilter"})
	public String removeIndexBoxFromFilter(//
			@RequestParam(name = "index", required = false, defaultValue = "")
            int index//
 			, StandardSessionFacade session//
	) {
		IndexBoxes indexBoxAttrRefList = (IndexBoxes) session.getAttribute(Constants.sessionIndexBoxAttrRefList.name());
		IndexBoxAttrRef indexBox = indexBoxAttrRefList.get(index);
		indexBox.setFilterOn(false);
		indexBoxService.update(//
				(SessionAppUser) session.getAttribute(ControllerConstants.sessionAppUser.name())//
				, indexBox//
				, indexBox.getName()//
				, indexBox.getSubject()//
		);
		QuestionsWithAnswers questionsWithAnswers = findQuestionsWithAnswersList(indexBoxAttrRefList);
		//noch mal gucken, ob das wirklich nötig ist.
		session.setAttribute(Constants.sessionIndexBoxAttrRefList.name(), indexBoxAttrRefList);
		session.setAttribute(Constants.sessionQuestionsWithAnswers.name(), questionsWithAnswers);
		
		return Constants.manageQuestionsWithAnswersPage.name();
	}
	@RequestMapping({"controlLinkEditQuestionWithAnswer"})
	public String editQuestionWithAnswer(//
			@RequestParam(name = "index", required = false)
            int index//
			, Model model//
			, StandardSessionFacade session//
	) {
		QuestionsWithAnswers questionsWithAnswers = (QuestionsWithAnswers) session.getAttribute(Constants.sessionQuestionsWithAnswers.name());
		QuestionWithAnswerAttrRef questionWithAnswerAttrRef = questionsWithAnswers.get(index);
		return editQuestionWithAnswerController.showEditQuestionWithAnswerPage(questionWithAnswerAttrRef, model, session);
	}
	@RequestMapping(value="/controlActionManageQuestionsWithAnswers", method=RequestMethod.POST, params= {"createQuestionsWithAnswers"})
	public String createQuestionsWithAnswers(//
			Model model//
			, StandardSessionFacade session//
	) {
		return createQuestionsWithAnswersController.showCreateQuestionsWithAnswersPage(model, session);
	}
	@RequestMapping(value="/controlActionManageQuestionsWithAnswers", method=RequestMethod.POST, params= {"backToMenu"})
	public String backToMenu(Model model, StandardSessionFacade session) {
		session.removeAttribute(Constants.sessionIndexBoxAttrRefList.name());
		session.removeAttribute(Constants.sessionQuestionsWithAnswers.name());
		return menuController.showMenuPage(model, session);
	}
	@RequestMapping(value="/controlActionManageQuestionsWithAnswers", method=RequestMethod.POST, params= {"selectAll"})
	public String selectAll(Model model, StandardSessionFacade session) {
		return showManageQuestionsWithAnswersPage(model, session);
	}
	@RequestMapping(value="/controlActionManageQuestionsWithAnswers", method=RequestMethod.POST, params= {"deselectAll"})
	public String deselectAll(Model model, StandardSessionFacade session) {
		QuestionsWithAnswers questionsWithAnswers = (QuestionsWithAnswers)session.getAttribute(Constants.sessionQuestionsWithAnswers.name());
		for (QuestionWithAnswerAttrRef questionWithAnswerAttrRef : questionsWithAnswers) {
			questionWithAnswerAttrRef.setFilterOn(false);
		}
		return Constants.manageQuestionsWithAnswersPage.name();
	}
}
