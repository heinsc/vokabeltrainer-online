package de.heins.vokabeltraineronline.web.controller;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.heins.vokabeltraineronline.business.service.IndexBoxService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.QuestionWithAnswerAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.ManageQuestionsWithAnswersModAtt;

@Controller
public class ManageQuestionsWithAnswersController {
	private static enum Constants{
		manageQuestionsWithAnswersModAtt
		, sessionIndexBoxAttrRefList
		, manageQuestionsWithAnswersPage
	}
	@Autowired
	private IndexBoxService indexBoxService;

	public ManageQuestionsWithAnswersController() {
		super();
	}

	@RequestMapping({ "/controlPageManageQuestionsWithAnswers" })
	public String showManageQuestionsWithAnswersPage(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		ManageQuestionsWithAnswersModAtt manageQuestionsWithAnswersModAtt = new ManageQuestionsWithAnswersModAtt();
		
		List<IndexBoxAttrRef> indexBoxAttrRefList = indexBoxService.findAllForAppUser(sessionAppUser);
		session.setAttribute(Constants.sessionIndexBoxAttrRefList.name(), indexBoxAttrRefList);
		Set<QuestionWithAnswerAttrRef> questionsWithAnswersList = findQuestionsWithAnswersList(indexBoxAttrRefList);
		manageQuestionsWithAnswersModAtt.setQuestionsWithAnswers(questionsWithAnswersList);
		model.addAttribute(Constants.manageQuestionsWithAnswersModAtt.name(), manageQuestionsWithAnswersModAtt);
		return Constants.manageQuestionsWithAnswersPage.name();
	}

	private Set<QuestionWithAnswerAttrRef> findQuestionsWithAnswersList(List<IndexBoxAttrRef> indexBoxAttrRefList) {
		Set<QuestionWithAnswerAttrRef> allQuestionsWithAnswers = new HashSet<QuestionWithAnswerAttrRef>();
		for (IndexBoxAttrRef indexBoxAttrRef : indexBoxAttrRefList) {
			if (indexBoxAttrRef.isFilterOn()) {
				allQuestionsWithAnswers.addAll(//
						indexBoxAttrRef.getQuestionsWithAnswers()//
				);
			}
		}
		return allQuestionsWithAnswers;
	}
	@RequestMapping({"controlLinkDeleteQuestionWithAnswer"})
	public String deleteQuestionWithAnswer(//
            @RequestParam(name = "question", required = false, defaultValue = "")
            String question//
			, StandardSessionFacade session//
	) throws Exception {
	    session.setAttribute(//
	    		ControllerConstants.sessionOldVersionOfQuestion.name()//
	    		, question//
	    );
	    return "redirect:" + ControllerConstants.controlPageManageQuestionsWithAnswers;
//		TODO return "redirect:" + ControllerConstants.controlDeleteQuestion.name();
	}
	@RequestMapping({"controlLinkAddIndexBoxToFilter"})
	public String addIndexBoxToFilter(//
			@RequestParam(name = "index", required = false, defaultValue = "")
            int index//
            , Model model
			, StandardSessionFacade session//
	) throws Exception {
		List<IndexBoxAttrRef> indexBoxAttrRefList = (List<IndexBoxAttrRef>) session.getAttribute(Constants.sessionIndexBoxAttrRefList.name());
		IndexBoxAttrRef indexBox = indexBoxAttrRefList.get(index);
		indexBox.setFilterOn(true);
		Set<QuestionWithAnswerAttrRef> questionsWithAnswers = findQuestionsWithAnswersList(indexBoxAttrRefList);
		//noch mal gucken, ob das wirklich nötig ist.
		session.setAttribute(Constants.sessionIndexBoxAttrRefList.name(), indexBoxAttrRefList);
		ManageQuestionsWithAnswersModAtt manageQuestionsWithAnswersModAtt = new ManageQuestionsWithAnswersModAtt();
		manageQuestionsWithAnswersModAtt.setQuestionsWithAnswers(questionsWithAnswers);
		model.addAttribute(Constants.manageQuestionsWithAnswersModAtt.name(), manageQuestionsWithAnswersModAtt);
		
		return Constants.manageQuestionsWithAnswersPage.name();
	}
	@RequestMapping({"controlLinkRemoveIndexBoxFromFilter"})
	public String removeIndexBoxFromFilter(//
			@RequestParam(name = "index", required = false, defaultValue = "")
            int index//
            , Model model
			, StandardSessionFacade session//
	) throws Exception {
		List<IndexBoxAttrRef> indexBoxAttrRefList = (List<IndexBoxAttrRef>) session.getAttribute(Constants.sessionIndexBoxAttrRefList.name());
		IndexBoxAttrRef indexBox = indexBoxAttrRefList.get(index);
		indexBox.setFilterOn(false);
		Set<QuestionWithAnswerAttrRef> questionsWithAnswers = findQuestionsWithAnswersList(indexBoxAttrRefList);
		//noch mal gucken, ob das wirklich nötig ist.
		session.setAttribute(Constants.sessionIndexBoxAttrRefList.name(), indexBoxAttrRefList);
		ManageQuestionsWithAnswersModAtt manageQuestionsWithAnswersModAtt = new ManageQuestionsWithAnswersModAtt();
		manageQuestionsWithAnswersModAtt.setQuestionsWithAnswers(questionsWithAnswers);
		model.addAttribute(Constants.manageQuestionsWithAnswersModAtt.name(), manageQuestionsWithAnswersModAtt);
		
		return Constants.manageQuestionsWithAnswersPage.name();
	}
	@RequestMapping({"controlLinkEditQuestionWithAnswer"})
	public String editQuestionWithAnswer(//
			@RequestParam(name = "question", required = false, defaultValue = "")
            String question//
			, StandardSessionFacade session//
	) throws Exception {
		session.setAttribute(ControllerConstants.sessionOldVersionOfQuestion.name(), question);
		
		return "redirect:" + ControllerConstants.controlPageEditQuestionWithAnswer.name();
	}
	@RequestMapping(value="/controlActionManageQuestionsWithAnswers", method=RequestMethod.POST, params= {"createQuestionsWithAnswers"})
	public String createQuestionsWithAnswers(//
	) throws Exception {
		return "redirect:" + ControllerConstants.controlPageCreateQuestionsWithAnswers.name();
	}
	@RequestMapping(value="/controlActionManageQuestionsWithAnswers", method=RequestMethod.POST, params= {"backToMenu"})
	public String backToMenu() {
		//direct go to mainMenu
		return "redirect:" +ControllerConstants.controlPageMenu.name();
	}
	@RequestMapping(value="/controlActionManageQuestionsWithAnswers", method=RequestMethod.POST, params= {"logout"})
	public String logout() {
		//direct go to login 
		return "redirect:" + ControllerConstants.controlPageLogin.name();
	}
}
