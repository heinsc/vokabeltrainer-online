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
public class LearnDoLearnController {
	private static enum Constants {
		learnDoLearnPage
		, learnDoLearnModAtt
		, indicatorToMakePoolEmpty
		, declareCorrectDespiteErrorsPage
		, sessionPoolWithWrongAnswers
		, sessionStockOfAllQuestionsWithAnswers	}
	@Autowired
	private LearnFilterIndexBoxesController learnFilterIndexBoxesController;
	@Autowired
	private MenuController menuController;
	@Autowired
	private QuestionWithAnswerService questionWithAnswerService;
	@Autowired
	private AnswerIsCorrectController answerIsCorrectController;
	@Autowired
	private AnswerIsIncorrectController answerIsIncorrectController;
	@Autowired
	private LearnProfileService learnProfileService;
	@Autowired
	private SuccessStepService successStepService;
	@Autowired
	private FaultToleranceService faultToleranceService;

	public LearnDoLearnController() {
		super();
	}

	public String showLearnDoLearnPages(//
			Model model//
			, StandardSessionFacade session//
	) {
		return goToLearnDoLearnPage(model, session);
	}

	private String goToLearnDoLearnPage(Model model, StandardSessionFacade session) {
		SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		QuestionWithAnswerAttrRef questionWithAnswerAttrRef;
		List<QuestionWithAnswerAttrRef> poolWithWrongAnwers = manageSessionPoolOfWrongAnswers(session);
		List<QuestionWithAnswerAttrRef> stockOfAllQuestionsWithAnswers = manageStockOfAllQuestions(session);
		if (poolWithWrongAnwers.isEmpty()) {
			session.setAttribute(Constants.indicatorToMakePoolEmpty.name(), false);
			if  (stockOfAllQuestionsWithAnswers.isEmpty()) {
				return backToLearnFilterIndexBoxes(model, session);
			} else {
				questionWithAnswerAttrRef = stockOfAllQuestionsWithAnswers.get(0);
			}
		} else {
			LearnProfileAttrRef learnProfile = learnProfileService.findLearnProfileByUser(sessionAppUser);
			if (poolWithWrongAnwers.size() >= learnProfile.getMaxNumberOfWrongAnswersPerSession()) {
				questionWithAnswerAttrRef = poolWithWrongAnwers.get(0);
				if (BehaviourIfPoolWithWrongAnswersIsFull.EMPTY_POOL_UNTIL_ALL_QUESTIONS_CORRECT.name().equals(learnProfile.getBehaviourIfPoolWithWrongAnswersIsFull())) {
					session.setAttribute(Constants.indicatorToMakePoolEmpty.name(), true);
				}
			} else {
				//pool is neither full nor emtpy
				if ((Boolean) session.getAttribute(Constants.indicatorToMakePoolEmpty.name())) {
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
		session.setAttribute(ControllerConstants.sessionActualAskedQuestion.name(),  questionWithAnswerAttrRef);
		LearnDoLearnModAtt learnDoLearnModAtt = new LearnDoLearnModAtt();
		model.addAttribute(Constants.learnDoLearnModAtt.name(), learnDoLearnModAtt);
		return Constants.learnDoLearnPage.name();
	}

	private String backToLearnFilterIndexBoxes(Model model, StandardSessionFacade session) {
		cleanSessionAttributes(session);
		return learnFilterIndexBoxesController.showLearnFilterIndexBoxesPage(session, model);
	}

	private void cleanSessionAttributes(StandardSessionFacade session) {
		session.removeAttribute(Constants.indicatorToMakePoolEmpty.name());
		session.removeAttribute(Constants.sessionPoolWithWrongAnswers.name());
		session.removeAttribute(Constants.sessionStockOfAllQuestionsWithAnswers.name());
	}

	private PoolWithWrongAnswers manageSessionPoolOfWrongAnswers(StandardSessionFacade session) {
		Object tempPool = session.getAttribute(Constants.sessionPoolWithWrongAnswers.name());
		PoolWithWrongAnswers pool;
		if (null != tempPool) {
			pool = (PoolWithWrongAnswers) tempPool;
		} else {
			pool = new PoolWithWrongAnswers();
			session.setAttribute(Constants.sessionPoolWithWrongAnswers.name(), pool);
		}
		return pool;
	}

	private List<QuestionWithAnswerAttrRef> manageStockOfAllQuestions(//
			StandardSessionFacade session//
	) {
		StockOfAllQuestionsWithAnswers  stockOfAllQuestionsWithAnswers
			= (StockOfAllQuestionsWithAnswers)session.getAttribute(//
					Constants.sessionStockOfAllQuestionsWithAnswers.name()//
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
					Constants.sessionStockOfAllQuestionsWithAnswers.name()//
					, stockOfAllQuestionsWithAnswers//
		);
		}
		return stockOfAllQuestionsWithAnswers;
	}
	
	@RequestMapping(value = "/controlActionLearnDoLearn", method = RequestMethod.POST, params = {"checkAnswer"})
	public String checkAnswer(//
			Model model// for the following sites, that they have a model.
			, @ModelAttribute(name = "learnDoLearnModAtt")
			LearnDoLearnModAtt learnDoLearnModAtt//
			, StandardSessionFacade session//
	) {
		SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		
		QuestionWithAnswerAttrRef questionWithAnswerAttrRef//
			= (QuestionWithAnswerAttrRef) session.getAttribute(//
					ControllerConstants.sessionActualAskedQuestion.name()//
			);
		PoolWithWrongAnswers pool
		= (PoolWithWrongAnswers) session.getAttribute(//
				Constants.sessionPoolWithWrongAnswers.name()//
		);
		StockOfAllQuestionsWithAnswers stock
		= (StockOfAllQuestionsWithAnswers) session.getAttribute(//
				Constants.sessionStockOfAllQuestionsWithAnswers.name()//
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
			return answerIsCorrectController.showAnswerIsCorrectPage();
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
						ControllerConstants.sessionActualAskedQuestion.name()//
						, questionWithAnswerAttrRef//
				);
				session.setAttribute(ControllerConstants.sessionYourAnswer.name(), learnDoLearnModAtt.getAnswerByUser());
				return goToDeclareCorrectDespiteErrorsPage();
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
		return answerIsIncorrectController.showAnswerIsIncorrectPage(//
				model//
				, questionWithAnswerAttrRef//
				, learnDoLearnModAtt.getAnswerByUser()//
		);

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
	public String cancel(Model model, StandardSessionFacade session) {
		cleanSessionAttributes(session);
		return menuController.showMenuPage(model, session);
	}
	@RequestMapping(value = "/controlActionLearnDoLearn", method = RequestMethod.POST, params = {"chooseOtherIndexBoxes"})
	public String chooseOtherIndexBoxes(Model model, StandardSessionFacade session)  {
		return backToLearnFilterIndexBoxes(model, session);
	}

	private String goToDeclareCorrectDespiteErrorsPage()  {
		return Constants.declareCorrectDespiteErrorsPage.name();
	}

	@RequestMapping(value = "/controlActionDeclareCorrectDespiteErrors", method = RequestMethod.POST, params = {"declareCorrect"})
	public String declareAsCorrect(//
			Model model//
			, StandardSessionFacade session
	) {
		QuestionWithAnswerAttrRef questionWithAnswerAttrRef//
			= (QuestionWithAnswerAttrRef)session.getAttribute(//
					ControllerConstants.sessionActualAskedQuestion.name()//
			);
		SessionAppUser sessionAppUser = (SessionAppUser) session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		PoolWithWrongAnswers pool = (PoolWithWrongAnswers) session.getAttribute(//
				Constants.sessionPoolWithWrongAnswers.name()//
		);
		StockOfAllQuestionsWithAnswers stock = (StockOfAllQuestionsWithAnswers) session.getAttribute(//
				Constants.sessionStockOfAllQuestionsWithAnswers.name()//
		);
		questionWithAnswerService.markAsAnsweredCorrectAndSave(//
				questionWithAnswerAttrRef//
				, sessionAppUser//
				, pool//
				, stock//
		);
		return showLearnDoLearnPages(model, session);
	}

	@RequestMapping(value = "/controlActionDeclareCorrectDespiteErrors", method = RequestMethod.POST, params = {"declareIncorrect"})
		public String declareAsIncorrect(//
				Model model//
				, StandardSessionFacade session
				) {
			QuestionWithAnswerAttrRef sessionQuestionWithAnswerAttrRef//
			= (QuestionWithAnswerAttrRef)session.getAttribute(//
					ControllerConstants.sessionActualAskedQuestion.name()//
			);
		SessionAppUser sessionAppUser = (SessionAppUser) session.getAttribute(ControllerConstants.sessionAppUser.name());
		PoolWithWrongAnswers poolOfQuestionsWithIncorrectAnswer = (PoolWithWrongAnswers) session.getAttribute(//
				Constants.sessionPoolWithWrongAnswers.name()//
		);
		StockOfAllQuestionsWithAnswers stockOfAllNonAskedQuestions = (StockOfAllQuestionsWithAnswers) session.getAttribute(//
				Constants.sessionStockOfAllQuestionsWithAnswers.name()//
		);
		questionWithAnswerService.markAsAnsweredIncorrectlyAndSave(//
				sessionQuestionWithAnswerAttrRef//
				, sessionAppUser//
				, stockOfAllNonAskedQuestions//
				, poolOfQuestionsWithIncorrectAnswer//
		);
		return goToLearnDoLearnPage(model, session);
	}

}