package de.heins.vokabeltraineronline.web.controller;

import java.util.List;
import java.util.Set;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.heins.vokabeltraineronline.business.entity.BehaviourIfPoolWithWrongAnswersIsFull;
import de.heins.vokabeltraineronline.business.service.FaultToleranceService;
import de.heins.vokabeltraineronline.business.service.LearnProfileService;
import de.heins.vokabeltraineronline.business.service.QuestionWithAnswerService;
import de.heins.vokabeltraineronline.business.service.SuccessStepService;
import de.heins.vokabeltraineronline.web.entities.IndexBoxes;
import de.heins.vokabeltraineronline.web.entities.PoolWithWrongAnswers;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.StockOfAllQuestionsWithAnswers;
import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.LearnProfileAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.QuestionWithAnswerAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.SuccessStepAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.LearnDoLearnModAtt;

@Controller
@SessionAttributes("learnDoLearnModAtt")
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
	@Autowired
	private SuccessStepService successStepService;
	@Autowired
	private FaultToleranceService faultToleranceService;

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
				questionWithAnswerAttrRef = stockOfAllQuestionsWithAnswers.get(0);
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
						questionWithAnswerAttrRef = stockOfAllQuestionsWithAnswers.get(0);
					}
				}
			}
		}
		LearnDoLearnModAtt learnDoLearnModAtt = new LearnDoLearnModAtt();
		learnDoLearnModAtt.setQuestionWithAnswerAttrRef(questionWithAnswerAttrRef);
		model.addAttribute(Constants.learnDoLearnModAtt.name(), learnDoLearnModAtt);
		return Constants.learnDoLearnPage.name();

	}

	private PoolWithWrongAnswers manageSessionPoolOfWrongAnswers(StandardSessionFacade session) {
		Object tempPool = session.getAttribute(ControllerConstants.sessionPoolWithWrongAnswers.name());
		PoolWithWrongAnswers pool;
		if (null != tempPool) {
			pool = (PoolWithWrongAnswers) tempPool;
		} else {
			pool = new PoolWithWrongAnswers();
			session.setAttribute(ControllerConstants.sessionPoolWithWrongAnswers.name(), pool);
		}
		return pool;
	}

	private List<QuestionWithAnswerAttrRef> manageStockOfAllQuestions(//
			StandardSessionFacade session//
	) {
		StockOfAllQuestionsWithAnswers  stockOfAllQuestionsWithAnswers
			= (StockOfAllQuestionsWithAnswers)session.getAttribute(//
					ControllerConstants.sessionStockOfAllQuestionsWithAnswers.name()//
		);
		if (//
				stockOfAllQuestionsWithAnswers == null
				|| stockOfAllQuestionsWithAnswers.isEmpty()//
		) {
			SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
					ControllerConstants.sessionAppUser.name()//
			);
			IndexBoxes indexBoxAttrRefList = (IndexBoxes) session.getAttribute(ControllerConstants.sessionIndexBoxAttrRefList.name());
			stockOfAllQuestionsWithAnswers = new StockOfAllQuestionsWithAnswers();
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
					ControllerConstants.sessionStockOfAllQuestionsWithAnswers.name()//
					, stockOfAllQuestionsWithAnswers//
		);
		}
		return stockOfAllQuestionsWithAnswers;
	}
	
	@RequestMapping(value = "/controlActionLearnDoLearn", method = RequestMethod.POST, params = {"checkAnswer"})
	public String checkAnswer(//
			@ModelAttribute(name = "learnDoLearnModAtt")
			LearnDoLearnModAtt learnDoLearnModAtt//
			, StandardSessionFacade session//
	) throws Exception {
		SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		QuestionWithAnswerAttrRef questionWithAnswerAttrRef = learnDoLearnModAtt.getQuestionWithAnswerAttrRef();
		PoolWithWrongAnswers pool
		= (PoolWithWrongAnswers) session.getAttribute(//
				ControllerConstants.sessionPoolWithWrongAnswers.name()//
		);
		StockOfAllQuestionsWithAnswers stock
		= (StockOfAllQuestionsWithAnswers) session.getAttribute(//
				ControllerConstants.sessionStockOfAllQuestionsWithAnswers.name()//
		);
		if (//
				questionWithAnswerAttrRef.getAnswer()//
				.equals(learnDoLearnModAtt.getAnswerByUser())//
		) {
			// Zu diesem Zeitpunkt befindet sich die Frage entweder im Pool (der bereits falsch beantworteten Fragen)
			// oder im Stock (der noch nicht abgefragten fragen. Finde also die Frage in einer der beiden Listen, um
			// sie dort zu entfernen.
			questionWithAnswerService.markAsAnsweredCorrectAndSave(//
					questionWithAnswerAttrRef//
					, sessionAppUser//
					, pool//
					, stock//
			);
			return "redirect:" + ControllerConstants.controlPageAnswerIsCorrect.name();
		} else {
			if (//
					isEqualWithoutSpecialCharacters(//
							questionWithAnswerAttrRef//
							, learnDoLearnModAtt.getAnswerByUser()//
					) || isEqualDueToFaultTolerance(//
							questionWithAnswerAttrRef//
							, learnDoLearnModAtt.getAnswerByUser()//
							, sessionAppUser//
					)
			) {
				session.setAttribute(//
						ControllerConstants.sessionQuestionWithAnswerAttrRef.name()//
						, questionWithAnswerAttrRef//
				);
				session.setAttribute(ControllerConstants.sessionYourAnswer.name(), learnDoLearnModAtt.getAnswerByUser());
				return "redirect:" + ControllerConstants.controlPageDeclareCorrectDespiteErrors.name();
			}
		}
		// Wenn bis hier kein Ausstieg (return), ist die Antwort so falsch, dass sie wie falsch behandelt werden
		// muss.
		questionWithAnswerService.markAsAnsweredIncorrectlyAndSave(//
				questionWithAnswerAttrRef//
				, sessionAppUser//
				, stock//
				, pool//
		);
		return "redirect:" + ControllerConstants.controlPageAnswerIsIncorrect.name();

	}

	private boolean isEqualDueToFaultTolerance(//
			QuestionWithAnswerAttrRef questionWithAnswerAttrRef//
			, String answerByUser//
			, SessionAppUser sessionAppUser//
		) {
		SuccessStepAttrRef actualSuccessStep = successStepService.findForAppUserAndName(//
				sessionAppUser//
				, questionWithAnswerAttrRef.getActualSuccessStepDescription()//
		);
		if (SuccessStepService.EMPTY_SUCCESS_STEP != actualSuccessStep) {
			String faultTolerance = actualSuccessStep.getFaultTolerance();
			return faultToleranceService.checkOnEqualnessOfCurrentIndexPair(//
					questionWithAnswerAttrRef.getAnswer()//
					, answerByUser//
					, faultTolerance//
			);
		}
		// before first success step or without any success step is always orally
		return true;
	}
	private boolean isEqualWithoutSpecialCharacters(//
			QuestionWithAnswerAttrRef questionWithAnswerAttrRef
			, String answerByUser//
	) {
		String answer = questionWithAnswerAttrRef.getAnswer();
		String answerOnlyWordCharacter = answer.replaceAll("[^\\p{L}\\p{Nd}]+", "");
		String answerByUserOnlyWordCharacter = answerByUser.replaceAll("[^\\p{L}\\p{Nd}]+", "");
		return answerOnlyWordCharacter.equals(answerByUserOnlyWordCharacter);
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
		return "redirect:" + ControllerConstants.controlPageLogin.name();

	}


}