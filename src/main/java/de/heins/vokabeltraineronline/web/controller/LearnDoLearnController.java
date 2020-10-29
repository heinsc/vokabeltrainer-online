package de.heins.vokabeltraineronline.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.heins.vokabeltraineronline.business.entity.BehaviourIfPoolWithWrongAnswersIsFull;
import de.heins.vokabeltraineronline.business.service.LearnProfileService;
import de.heins.vokabeltraineronline.business.service.QuestionWithAnswerService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.LearnProfileAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.QuestionWithAnswerAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.LearnDoLearnModAtt;

@Controller
public class LearnDoLearnController {
	private static final String INDICATOR_TO_MAKE_POOL_EMPTY = "indicatorToMakePoolEmpty";
	private static enum Constants {
		learnDoLearnPage
		, learnDoLearnModAtt
	}
	@Autowired
	private QuestionWithAnswerService questionWithAnswerService;
	@Autowired
	private LearnProfileService learnProfileService;
	public LearnDoLearnController() {
		super();
	}

	@RequestMapping("/controlPageLearnDoLearn")
	public String showLearnDoLearnPage(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		QuestionWithAnswerAttrRef questionWithAnswerAttrRef;
		List<QuestionWithAnswerAttrRef> poolWithWrongAnwers = manageSessionPoolOfWrongAnswers(session);
		List<QuestionWithAnswerAttrRef> stockOfAllQuestionsWithAnswers = manageStockOfAllQuestions(session);
		if (poolWithWrongAnwers.isEmpty()) {
			session.setAttribute(INDICATOR_TO_MAKE_POOL_EMPTY, false);
			if  (stockOfAllQuestionsWithAnswers.isEmpty()) {
				return "redirect:" + ControllerConstants.controlPageLearnFilterIndexBoxes.name();
			} else {
				questionWithAnswerAttrRef = takeFromStock(stockOfAllQuestionsWithAnswers, poolWithWrongAnwers);
			}
		} else {
			LearnProfileAttrRef learnProfile = learnProfileService.findLearnProfileByUser(sessionAppUser);
			if (poolWithWrongAnwers.size() >= learnProfile.getMaxNumberOfWrongAnswersPerSession()) {
				questionWithAnswerAttrRef = poolWithWrongAnwers.get(0);
				if (learnProfile.getBehaviourIfPoolWithWrongAnswersIsFull().equals(BehaviourIfPoolWithWrongAnswersIsFull.EMPTY_POOL_UNTIL_ALL_QUESTIONS_CORRECT)) {
					session.setAttribute(INDICATOR_TO_MAKE_POOL_EMPTY, true);
				}
			} else {
				//pool is neither full nor emtpy
				if ((Boolean) session.getAttribute(INDICATOR_TO_MAKE_POOL_EMPTY)) {
					questionWithAnswerAttrRef = poolWithWrongAnwers.get(0);
				} else {
					if  (stockOfAllQuestionsWithAnswers.isEmpty()) {
						questionWithAnswerAttrRef = poolWithWrongAnwers.get(0);
					} else {
						questionWithAnswerAttrRef = takeFromStock(stockOfAllQuestionsWithAnswers, poolWithWrongAnwers);
					}
				}
			}
		}
		LearnDoLearnModAtt learnDoLearnModAtt = new LearnDoLearnModAtt();
		learnDoLearnModAtt.setQuestionWithAnswerAttrRef(questionWithAnswerAttrRef);
		model.addAttribute(Constants.learnDoLearnModAtt.name(), learnDoLearnModAtt);
		return Constants.learnDoLearnPage.name();

	}

	private QuestionWithAnswerAttrRef takeFromStock(//
			List<QuestionWithAnswerAttrRef> stockOfAllQuestionsWithAnswers//
			, List<QuestionWithAnswerAttrRef> poolWithWrongAnwers//
		) {
		QuestionWithAnswerAttrRef questionWithAnswerAttrRef = stockOfAllQuestionsWithAnswers.remove(0);
		poolWithWrongAnwers.add(questionWithAnswerAttrRef);
		return questionWithAnswerAttrRef;
	}

	private List<QuestionWithAnswerAttrRef> manageSessionPoolOfWrongAnswers(StandardSessionFacade session) {
		Object tempPool = session.getAttribute(ControllerConstants.sessionPoolWithWrongAnswers.name());
		List<QuestionWithAnswerAttrRef> pool;
		if (null != tempPool) {
			pool = (List<QuestionWithAnswerAttrRef>) tempPool;
		} else {
			pool = new ArrayList<QuestionWithAnswerAttrRef>();
			session.setAttribute(ControllerConstants.sessionPoolWithWrongAnswers.name(), pool);
		}
		return pool;
	}

	private List<QuestionWithAnswerAttrRef> manageStockOfAllQuestions(//
			StandardSessionFacade session//
	) {
		List<QuestionWithAnswerAttrRef>  stockOfAllQuestionsWithAnswers
			= (List<QuestionWithAnswerAttrRef>)session.getAttribute(//
					"stockOfAllQuestionsWithAnswers"//
		);
		if (//
				stockOfAllQuestionsWithAnswers == null
				|| stockOfAllQuestionsWithAnswers.isEmpty()//
		) {
			SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
					ControllerConstants.sessionAppUser.name()//
			);
			List<IndexBoxAttrRef> indexBoxAttrRefList = (List<IndexBoxAttrRef>) session.getAttribute(ControllerConstants.sessionIndexBoxAttrRefList.name());
			stockOfAllQuestionsWithAnswers = new ArrayList<QuestionWithAnswerAttrRef>();
			for (IndexBoxAttrRef currentIndexBoxAttrRef : indexBoxAttrRefList) {
				if (currentIndexBoxAttrRef.isFilterOn()) {
					Set<QuestionWithAnswerAttrRef> questionsWithAnswersOfCurrentIndexBox = currentIndexBoxAttrRef
							.getQuestionsWithAnswers();
					for (QuestionWithAnswerAttrRef currentQuestionWithAnswerAttrRef : questionsWithAnswersOfCurrentIndexBox) {
						if (questionWithAnswerService.hasToBeAskedNow(currentQuestionWithAnswerAttrRef,
								sessionAppUser)) {
							stockOfAllQuestionsWithAnswers.add(currentQuestionWithAnswerAttrRef);
						}
					} 
				}
			}
 			session.setAttribute(//
					"stockOfAllQuestionsWithAnswers"//
					, stockOfAllQuestionsWithAnswers//
		);
		}
		return stockOfAllQuestionsWithAnswers;
	}
	@RequestMapping(value = "/controlActionLearnDoLearn", method = RequestMethod.POST, params = {"cancel"})
	public String cancel() throws Exception {
		return "redirect:" + ControllerConstants.controlPageMenu.name();

	}
	@RequestMapping(value = "/controlActionLearnDoLearn", method = RequestMethod.POST, params = {"chooseOtherIndexBoxes"})
	public String chooseOtherIndexBoxes() throws Exception {
		return "redirect:" + ControllerConstants.controlPageLearnFilterIndexBoxes.name();

	}
	@RequestMapping(value = "/controlActionLearnDoLearn", method = RequestMethod.POST, params = {"logout"})
	public String logout() throws Exception {
		return "redirect:" + ControllerConstants.controlPageLearnFilterIndexBoxes.name();

	}


}