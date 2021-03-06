package de.heins.vokabeltraineronline.business.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltraineronline.business.entity.LearningStrategy;
import de.heins.vokabeltraineronline.business.entity.LearningStrategyFactory;
import de.heins.vokabeltraineronline.business.entity.QuestionWithAnswer;
import de.heins.vokabeltraineronline.business.entity.SuccessStep;
import de.heins.vokabeltraineronline.business.entity.AppUser;
import de.heins.vokabeltraineronline.business.repository.LearningStrategyRepository;
import de.heins.vokabeltraineronline.business.repository.QuestionWithAnswerRepository;
import de.heins.vokabeltraineronline.business.repository.SuccessStepRepository;
import de.heins.vokabeltraineronline.business.repository.AppUserRepository;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.LearningStrategyAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.QuestionWithAnswerAttrRef;
@Service
public class LearningStrategyService {
	public static final LearningStrategyAttrRef EMPTY_LEARNING_STRATEGY = new LearningStrategyAttrRef();
	@Autowired
	private LearningStrategyRepository learningStrategyRepository;
	@Autowired
	private SuccessStepRepository successStepRepository;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private LearningStrategyFactory learningStrategyFactory;
	@Autowired
	private QuestionWithAnswerRepository questionWithAnswerRepository;
	public LearningStrategyAttrRef getForLearningStrategy(LearningStrategy learningStrategy) {
		LearningStrategyAttrRef learningStrategyAttrRef = new LearningStrategyAttrRef();
		learningStrategyAttrRef.setName(learningStrategy.getName());
		for (SuccessStep currentSuccessStep : learningStrategy.getSuccessSteps()) {
			learningStrategyAttrRef.getAssignedSuccessSteps().add(currentSuccessStep.getName());
		}
		return learningStrategyAttrRef;
	}
	public List<LearningStrategyAttrRef> findAllForAppUser(SessionAppUser sessionAppUserForm) {
		List<AppUser> appUsers = appUserRepository.findByEmail(sessionAppUserForm.getEmail());
		if (appUsers.size() == 1) {
			AppUser appUser = appUsers.get(0);
			List<LearningStrategyAttrRef> learningStrategyForms = new ArrayList<LearningStrategyAttrRef>();
			try {
				List<LearningStrategy> learningStrategies = learningStrategyRepository.findByAppUser(appUser);
				learningStrategies.forEach(//
						learningStrategy -> {
							LearningStrategyAttrRef learningStrategyAttRef = new LearningStrategyAttrRef();
							learningStrategyAttRef.setName(learningStrategy.getName());
							learningStrategyForms.add(learningStrategyAttRef);
							List<SuccessStep> successSteps = learningStrategy.getSuccessSteps();
							successSteps.forEach(//
									successStep -> learningStrategyAttRef//
										.getAssignedSuccessSteps()//
										.add(successStep.getName())
							);
						}
				);
			} catch (Exception e) {
				// this occurs only when there are no items in the database table,
				// or the table wasn't created yet.
				// nothing to do then.
			}
			return learningStrategyForms;
		} else {
			throw new RuntimeException("No AppUser found or AppUser not unique by email");
		}
	}
	public LearningStrategyAttrRef findForAppUserAndName(//
			SessionAppUser sessionAppUser//
			, String oldVersionOfLearningStrategyName//
	) {
		AppUser appUser = appUserRepository.findByEmail(sessionAppUser.getEmail()).get(0);

		try {
			List<LearningStrategy> learningStrategies = learningStrategyRepository.findByAppUserAndName(appUser, oldVersionOfLearningStrategyName);
			if (learningStrategies.size() == 1) {
				LearningStrategyAttrRef learningStrategyAttrRef = new LearningStrategyAttrRef();
				LearningStrategy learningStrategy = learningStrategies.get(0);
				learningStrategyAttrRef.setName(learningStrategy.getName());
				//TODO if the list is empty we need to find all SuccessSteps for this learniningStrategy 
				learningStrategy.getSuccessSteps().forEach(//
						currentSuccessStep -> learningStrategyAttrRef.getAssignedSuccessSteps().add(currentSuccessStep.getName())//
				);
				return learningStrategyAttrRef;
			}
		} catch (Exception e) {
			// this occurs only when there are no items in the database table,
			// or the table wasn't created yet.
			// nothing to do then.
		}
		return EMPTY_LEARNING_STRATEGY;
	}
	public void update(SessionAppUser appUserForm, LearningStrategyAttrRef learningStrategyAttrRef, String oldName) {
		AppUser appUser = appUserRepository.findByEmail(appUserForm.getEmail()).get(0);
		List<LearningStrategy> findByAppUserAndNameList = new ArrayList<LearningStrategy>();
		try {
			findByAppUserAndNameList = learningStrategyRepository.findByAppUserAndName(appUser, oldName);
		} catch (Exception e) {
			// this occurs only if there are no items in the database table,
			// or the table wasn't created yet.
			// nothing to do then.
		}
		LearningStrategy learningStrategy;
		if (findByAppUserAndNameList.isEmpty()) {//
				learningStrategy = learningStrategyFactory//
					.setName(learningStrategyAttrRef.getName())//
					.setAppUser(appUser)//
					.getNewObject();
				refreshRecentlyAssignedSuccessSteps(//
						learningStrategy//
						, learningStrategyAttrRef//
						, appUser//
				);
				
		} else {
			learningStrategy = findByAppUserAndNameList.get(0); //
			learningStrategy.setName(learningStrategyAttrRef.getName());//
			refreshRecentlyAssignedSuccessSteps(//
					learningStrategy//
					, learningStrategyAttrRef//
					, appUser//
			);
		}
		learningStrategyRepository.save(learningStrategy);
	}
	private void refreshRecentlyAssignedSuccessSteps(//
			LearningStrategy learningStrategy//
			, LearningStrategyAttrRef learningStrategyAttrRef//
			, AppUser appUser//
	) {
		learningStrategy.getSuccessSteps().clear();
		learningStrategyAttrRef.getAssignedSuccessSteps().forEach(
				currentSuccessStep -> learningStrategy.getSuccessSteps().add(//
						successStepRepository.findByAppUserAndName(appUser, currentSuccessStep).get(0)
				)
		);
	}
	public void resolveAndSaveNextSuccessStepp(//
			QuestionWithAnswerAttrRef sessionQuestionWithAnswerAttrRef//
			, SessionAppUser sessionAppUser//
	) {
		String learningStrategyDescription = sessionQuestionWithAnswerAttrRef.getLearningStrategyDescription();
		String actualSuccessStepDescription = sessionQuestionWithAnswerAttrRef.getActualSuccessStepDescription();
		
		AppUser appUser = appUserRepository.findByEmail(sessionAppUser.getEmail()).get(0);
		LearningStrategy learningStrategy = learningStrategyRepository.findByAppUserAndName(//
				appUser//
				, learningStrategyDescription//
		).get(0);
		List<SuccessStep> successStepsOfLearningStrategy = learningStrategy.getSuccessSteps();
		List<SuccessStep> actualSuccessSteps = successStepRepository.findByAppUserAndName(appUser, actualSuccessStepDescription);
		QuestionWithAnswer questionWithAnswer = questionWithAnswerRepository.findByAppUserAndQuestion(//
				appUser//
				, sessionQuestionWithAnswerAttrRef.getQuestion()//
		).get(0);
		if (actualSuccessSteps.isEmpty()) {
			resolveAndSaveNextSuccessStepAndAppearance(//
					0//
					, successStepsOfLearningStrategy//
					, questionWithAnswer//
			);
			questionWithAnswerRepository.save(questionWithAnswer);
		} else {
			SuccessStep actualSuccessStep = actualSuccessSteps.get(0);
			int indexOfActualSuccessStep = successStepsOfLearningStrategy.indexOf(actualSuccessStep);
			if (indexOfActualSuccessStep == successStepsOfLearningStrategy.size() - 1) {
				//last successStep was successful mastered
				questionWithAnswer.setActualSuccessStep(null);
				questionWithAnswer.setNextAppearance(IndexBoxService.LAST_SUCCESSSTEP_MADE_DATE);
				questionWithAnswerRepository.save(questionWithAnswer);
			} else {
				resolveAndSaveNextSuccessStepAndAppearance(//
						indexOfActualSuccessStep + 1//
						, successStepsOfLearningStrategy//
						, questionWithAnswer//
				);
				questionWithAnswerRepository.save(questionWithAnswer);
			}
		}
	}
	private void resolveAndSaveNextSuccessStepAndAppearance(int indexOfNextSuccessStep,
			List<SuccessStep> successStepsOfLearningStrategy, QuestionWithAnswer questionWithAnswer) {
		SuccessStep newActualSuccessStep = successStepsOfLearningStrategy.get(indexOfNextSuccessStep);
		int nextAppearanceInDays = newActualSuccessStep.getNextAppearanceInDays();
		Calendar nextAppearnceDate = Calendar.getInstance();
		nextAppearnceDate.add(Calendar.DAY_OF_MONTH, nextAppearanceInDays);
		questionWithAnswer.setNextAppearance(nextAppearnceDate.getTime());
		questionWithAnswer.setActualSuccessStep(newActualSuccessStep);
	}
}
