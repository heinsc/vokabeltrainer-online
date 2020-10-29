package de.heins.vokabeltraineronline.business.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltraineronline.business.repository.IndexBoxRepository;
import de.heins.vokabeltraineronline.business.repository.LearningStrategyRepository;
import de.heins.vokabeltraineronline.business.repository.QuestionWithAnswerRepository;
import de.heins.vokabeltraineronline.business.repository.AppUserRepository;
import de.heins.vokabeltraineronline.business.entity.IndexBox;
import de.heins.vokabeltraineronline.business.entity.LearningStrategy;
import de.heins.vokabeltraineronline.business.entity.QuestionWithAnswer;
import de.heins.vokabeltraineronline.business.entity.QuestionWithAnswerFactory;
import de.heins.vokabeltraineronline.business.entity.AppUser;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.QuestionWithAnswerAttrRef;


@Service
public class QuestionWithAnswerService {
	private static final int INDEXBOXNAME_SPLITTINGINDEX = 1;
	private static final int INDEXBOX_NAME_SPLITTINGINDEX = 0;
	public static final String INDEXBOX_DESCRIPTION_SPLITTER = ", ";
	public static final QuestionWithAnswerAttrRef EMPTY_QUESTION_WITH_ANSWER = new QuestionWithAnswerAttrRef();
	@Autowired
	private QuestionWithAnswerRepository questionWithAnswerRepository;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired 
	private LearningStrategyRepository learningStrategyRepository;
	@Autowired
	private IndexBoxRepository indexBoxRepository;
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
					.setAnswer(questionWithAnswerAttrRef.getAnswer())
					.setLearningStrategy(learningStrategy)//
					.setAppUser(appUser)//
					.getNewObject();
			//specialty: the indexBox is not part of the questionWithAnswer.
			// the indexBox must be changed and saved separately.
			indexBox.addQuestionWithAnswer(questionWithAnswer);
			questionWithAnswerRepository.save(questionWithAnswer);
			indexBoxRepository.save(indexBox);
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

}
