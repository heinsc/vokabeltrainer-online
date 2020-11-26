package de.heins.vokabeltraineronline.business.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltraineronline.business.repository.IndexBoxRepository;
import de.heins.vokabeltraineronline.business.repository.LearningStrategyRepository;
import de.heins.vokabeltraineronline.business.repository.QuestionWithAnswerRepository;
import de.heins.vokabeltraineronline.business.repository.SuccessStepRepository;
import de.heins.vokabeltraineronline.business.repository.AppUserRepository;
import de.heins.vokabeltraineronline.business.entity.IndexBox;
import de.heins.vokabeltraineronline.business.entity.LearningStrategy;
import de.heins.vokabeltraineronline.business.entity.QuestionWithAnswer;
import de.heins.vokabeltraineronline.business.entity.QuestionWithAnswerFactory;
import de.heins.vokabeltraineronline.business.entity.SuccessStep;
import de.heins.vokabeltraineronline.business.entity.AppUser;
import de.heins.vokabeltraineronline.business.entity.BehaviourIfWrong;
import de.heins.vokabeltraineronline.web.entities.PoolWithWrongAnswers;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.StockOfAllQuestionsWithAnswers;
import de.heins.vokabeltraineronline.web.entities.attributereference.QuestionWithAnswerAttrRef;


@Service
public class QuestionWithAnswerService {
	private static final int INDEXBOXNAME_SPLITTINGINDEX = 1;
	private static final int INDEXBOX_NAME_SPLITTINGINDEX = 0;
	public static final String INDEXBOX_DESCRIPTION_SPLITTER = ", ";
	public static final QuestionWithAnswerAttrRef EMPTY_QUESTION_WITH_ANSWER = new QuestionWithAnswerAttrRef();
	private static final String FIRST_SUCCESS_STEP_NOT_YET_REACHED = null;
	@Autowired
	private QuestionWithAnswerRepository questionWithAnswerRepository;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired 
	private LearningStrategyRepository learningStrategyRepository;
	@Autowired
	private IndexBoxRepository indexBoxRepository;
	@Autowired
	private SuccessStepRepository successStepRepository;
	@Autowired
	private QuestionWithAnswerFactory questionWithAnswerFactory;
	public void update(//
			SessionAppUser sessionAppUser//
			, QuestionWithAnswerAttrRef questionWithAnswerAttrRef//
			, String oldQuestion//
	) {
		AppUser appUser = appUserRepository.findByEmail(sessionAppUser.getEmail()).get(0);
		List<QuestionWithAnswer> findByAppUserAndQuestion = new ArrayList<QuestionWithAnswer>();
		try {
			findByAppUserAndQuestion = questionWithAnswerRepository.findByAppUserAndQuestion(appUser, oldQuestion);
		} catch (Exception e) {
			// this occurs only if there are no items in the database table,
			// or the table wasn't created yet.
			// nothing to do then.
		}
		QuestionWithAnswer questionWithAnswer;
		if (findByAppUserAndQuestion.isEmpty()) {//create a new questionWithAnswer
			IndexBox indexBox = findUniqueIndexBoxByAppUserAndDescription(appUser, questionWithAnswerAttrRef.getIndexBoxDescription());
			LearningStrategy learningStrategy = findUniqeLearningStrategyByName(questionWithAnswerAttrRef, appUser);
			questionWithAnswer = questionWithAnswerFactory//
					.setQuestion(questionWithAnswerAttrRef.getQuestion())//
					.setAnswer(questionWithAnswerAttrRef.getAnswer())//
					.setIndexBox(indexBox)//
					.setLearningStrategy(learningStrategy)//
					.setAppUser(appUser)//
					.getNewObject();
			questionWithAnswerRepository.save(questionWithAnswer);
		} else {//update existing questionWithAnswer
			questionWithAnswer = findByAppUserAndQuestion.get(0);
			questionWithAnswer.setQuestion(questionWithAnswerAttrRef.getQuestion());
			questionWithAnswer.setAnswer(questionWithAnswerAttrRef.getAnswer());
			// the learningStrategy of a questionWithAnswer can never be changed
			// moving questionWithAnswers from one index box to another is done by an extra function.
			questionWithAnswerRepository.save(questionWithAnswer);
		}
	}
	private LearningStrategy findUniqeLearningStrategyByName(QuestionWithAnswerAttrRef questionWithAnswerAttrRef,
			AppUser appUser) {
		List<LearningStrategy> learningStrategies = learningStrategyRepository.findByAppUserAndName(//
				appUser//
				, questionWithAnswerAttrRef.getLearningStrategyDescription()//
		);
		LearningStrategy learningStrategy = learningStrategies.get(0);
		return learningStrategy;
	}
	private IndexBox findUniqueIndexBoxByAppUserAndDescription(AppUser appUser, String indexBoxDescription) {
		String[] indexBoxKeys = indexBoxDescription.split(INDEXBOX_DESCRIPTION_SPLITTER);
		String indexBoxName = indexBoxKeys[INDEXBOX_NAME_SPLITTINGINDEX];
		String indexBoxSubject = indexBoxKeys[INDEXBOXNAME_SPLITTINGINDEX];
		List<IndexBox> indexBoxesByAppUserAndNameAndSubject = indexBoxRepository.findByAppUserAndNameAndSubject(//
				appUser//
				, indexBoxName//
				, indexBoxSubject//
		);
		IndexBox indexBox = indexBoxesByAppUserAndNameAndSubject.get(0);
		return indexBox;
	}
	public QuestionWithAnswerAttrRef findForAppUserAndQuestion(//
			SessionAppUser sessionAppUser//
			, String question//
	) {
		AppUser appUser = appUserRepository.findByEmail(//
				sessionAppUser.getEmail()
		).get(0);
		try {
			QuestionWithAnswer questionWithAnswer = questionWithAnswerRepository.findByAppUserAndQuestion(appUser, question).get(0);
			QuestionWithAnswerAttrRef questionWithAnswerAttrRef = new QuestionWithAnswerAttrRef();
			questionWithAnswerAttrRef.setQuestion(questionWithAnswer.getQuestion());
			questionWithAnswerAttrRef.setAnswer(questionWithAnswer.getAnswer());
			questionWithAnswerAttrRef.setLearningStrategyDescription(questionWithAnswer.getLearningStrategy().getName());
			if (null != questionWithAnswer.getActualSuccessStep()) {
				questionWithAnswerAttrRef.setActualSuccessStepDescription(QuestionWithAnswerService.FIRST_SUCCESS_STEP_NOT_YET_REACHED);
			} else {
				questionWithAnswerAttrRef.setActualSuccessStepDescription(questionWithAnswer.getActualSuccessStep().getName());
			}
			return questionWithAnswerAttrRef;
		} catch (Exception e) {
			// this occurs only if there are no items in the database table,
			// or the table wasn't created yet.
			// nothing to do then.
		}
		return EMPTY_QUESTION_WITH_ANSWER;
	}
	/**
	 * 
	 * @param currentQuestionWithAnswerAttrRef
	 * @param sessionAppUser
	 * @return true if the date of nextAppearance is in the past.
	 */
	public boolean hasToBeAskedNow(//
			QuestionWithAnswerAttrRef questionWithAnswerAttrRef//
			, SessionAppUser sessionAppUser//
	) {
		List<AppUser> findByEmail = appUserRepository.findByEmail(sessionAppUser.getEmail());
		AppUser user = findByEmail.get(0);
		List<QuestionWithAnswer> findByAppUserAndQuestion = questionWithAnswerRepository.findByAppUserAndQuestion(user, questionWithAnswerAttrRef.getQuestion());
		QuestionWithAnswer questionWithAnswer = findByAppUserAndQuestion.get(0);
		Calendar now = Calendar.getInstance();
		return now.getTime().after(questionWithAnswer.getNextAppearance());
	}
	public void markAsAnsweredCorrectAndSave(//
			QuestionWithAnswerAttrRef questionWithAnswerAttrRef//
			, SessionAppUser sessionAppUser//
			, PoolWithWrongAnswers pool//
			, StockOfAllQuestionsWithAnswers stock//
	) {
		AppUser appUser = appUserRepository.findByEmail(sessionAppUser.getEmail()).get(0);
		QuestionWithAnswer questionWithAnswer
			= questionWithAnswerRepository.findByAppUserAndQuestion(//
					appUser//
					, questionWithAnswerAttrRef.getQuestion()//
			).get(0);
		SuccessStep actualSuccessStep = questionWithAnswer.getActualSuccessStep();
		if (stock.remove(questionWithAnswerAttrRef)){//
			// Frage wurde direkt bei der ersten Abfrage richtig beantwortet.
			// ermittle neue Erfolgsstufe und das nächste Wiedervorlagedatum
			LearningStrategy learningStrategy = questionWithAnswer.getLearningStrategy();
			if (learningStrategy != null && learningStrategy.getSuccessSteps().size() != 0) {
				//Es sind Erfolgsstufen definiert
				List<SuccessStep> successSteps = learningStrategy.getSuccessSteps();
				if (successSteps.indexOf(actualSuccessStep) == successSteps.size() - 1) {
					// Es ist die letzte Erfolgsstufe
					questionWithAnswer.setActualSuccessStep(null);
					questionWithAnswer.setNextAppearance(IndexBoxService.LAST_SUCCESSSTEP_MADE_DATE);
				} else {
					// Es ist nicht die letzte Erfolgsstufe
					SuccessStep nextSuccessStep;
					if (actualSuccessStep == null) {
						// die erste Erfolgsstufe wurde noch nicht erreicht
						nextSuccessStep = successSteps.get(0);
					} else {
						// es ist eine Erfolgsstufe, aber nicht die letzte.
						nextSuccessStep = successSteps.get(successSteps.indexOf(actualSuccessStep) + 1);
					}
					questionWithAnswer.setActualSuccessStep(nextSuccessStep);
					int nextAppearanceInDays = nextSuccessStep.getNextAppearanceInDays();
					Calendar nextAppearance = Calendar.getInstance();
					nextAppearance.add(Calendar.DAY_OF_MONTH, nextAppearanceInDays);
					questionWithAnswer.setNextAppearance(nextAppearance.getTime());
				}
			} else {
				// Es sind gar keine Erfolgsstufen (Keine Lernstrategie -> keine Erfolgsstufen,
				// Lernstrategien ohne Erfolgsstufen gibt es nicht)
				questionWithAnswer.setNextAppearance(IndexBoxService.LAST_SUCCESSSTEP_MADE_DATE);
			}
		} else {
			// Wenn die Frage nicht im Stock ist, muss sie im Pool sein.
			// Damit wurde sie bereits mindestens ein Mal falsch beantwortet.
			// Das bedeutet, ihr wurde bereits und eine neue Erfolgsstufe
			// zugewiesen. Bevor sie aus dem Pool entfernt wird, muss noch
			// das neue Wiedervorlagedatum bestimmt werden.
			// Falls noch keine Erfolgsstufe erreicht wurde, oder wenn die nächste Dauer nicht
			// von der aktuellen Erfolgsstufe abhängen soll, definiere morgen als Wiedervorlage.
			int nextAppearanceInDays = 1;
			if (actualSuccessStep != null) {
				// Es wurde mindestens die erste Erfolgsschritt erreicht.
				BehaviourIfWrong behaviourIfWrong = actualSuccessStep.getBehaviourIfWrong();
				if (//
						behaviourIfWrong.equals(BehaviourIfWrong.PREVIOUS_SUCCESSSTEP_NEXTDAY_SUCCESSSTEP_DURATION)//
						|| behaviourIfWrong.equals(BehaviourIfWrong.STAY_SUCCESSSTEP_NEXTDAY_SUCCESSSTEP_DURATION)//
				) {
					nextAppearanceInDays = questionWithAnswer.getActualSuccessStep().getNextAppearanceInDays();
				}
			}
			Calendar nextAppearanceDay = Calendar.getInstance();
			nextAppearanceDay.add(Calendar.DAY_OF_MONTH, nextAppearanceInDays);
			questionWithAnswer.setNextAppearance(nextAppearanceDay.getTime());
			pool.remove(questionWithAnswerAttrRef);
			
		}
		questionWithAnswerRepository.save(questionWithAnswer);
	}
	public void markAsAnsweredIncorrectlyAndSave(//
			QuestionWithAnswerAttrRef sessionQuestionWithAnswerAttrRef//
			, SessionAppUser sessionAppUser//
			, StockOfAllQuestionsWithAnswers stockOfAllNonAskedQuestions
			, PoolWithWrongAnswers poolOfQuestionsWithIncorrectAnswer
	) {
		String actualSuccessStepDescription = sessionQuestionWithAnswerAttrRef.getActualSuccessStepDescription();
		AppUser appUser = appUserRepository.findByEmail(sessionAppUser.getEmail()).get(0);
		QuestionWithAnswer questionWithAnswer = questionWithAnswerRepository.findByAppUserAndQuestion(//
				appUser//
				, sessionQuestionWithAnswerAttrRef.getQuestion()//
		).get(0);
		List<SuccessStep> actualSuccessSteps = successStepRepository.findByAppUserAndName(appUser, actualSuccessStepDescription);
		if (!actualSuccessSteps.isEmpty()) {
			//At least the first accessStep has been reached.
			SuccessStep actualSuccessStep = actualSuccessSteps.get(0);
			BehaviourIfWrong behaviourIfWrong = actualSuccessStep.getBehaviourIfWrong();
			if (//
					behaviourIfWrong.equals(BehaviourIfWrong.PREVIOUS_SUCCESSSTEP_NEXTDAY_SUCCESSSTEP_DURATION)//
					|| behaviourIfWrong.equals(BehaviourIfWrong.PREVIOUS_SUCCESSTEP_NEXTDAY_TOMORROW)//
			) {
				String learningStrategyDescription = sessionQuestionWithAnswerAttrRef.getLearningStrategyDescription();
	
				LearningStrategy learningStrategy = learningStrategyRepository.findByAppUserAndName(//
						appUser//
						, learningStrategyDescription//
				).get(0);
				int indexOfActualSuccessStep = learningStrategy.getSuccessSteps().indexOf(actualSuccessStep);
				if (indexOfActualSuccessStep == 0) {
					questionWithAnswer.setActualSuccessStep(null);
				} else {
					questionWithAnswer.setActualSuccessStep(learningStrategy.getSuccessSteps().get(indexOfActualSuccessStep-1));
				}
			}
		}
		// wenn die Frage beim ersten Mal falsch war, transferiere sie vom Stock in den Pool
		// dass die Frage noch nicht gefragt wurde, ist daran erkennbar, dass Sie noch im Stock ist.
		if (stockOfAllNonAskedQuestions.remove(sessionQuestionWithAnswerAttrRef)) {
			poolOfQuestionsWithIncorrectAnswer.add(sessionQuestionWithAnswerAttrRef);
		}
	}

}
