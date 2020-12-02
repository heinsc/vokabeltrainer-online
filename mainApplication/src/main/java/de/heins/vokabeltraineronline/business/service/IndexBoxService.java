package de.heins.vokabeltraineronline.business.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltraineronline.business.entity.AppUser;
import de.heins.vokabeltraineronline.business.entity.IndexBox;
import de.heins.vokabeltraineronline.business.entity.IndexBoxFactory;
import de.heins.vokabeltraineronline.business.entity.QuestionWithAnswer;
import de.heins.vokabeltraineronline.business.entity.SuccessStep;
import de.heins.vokabeltraineronline.business.repository.AppUserRepository;
import de.heins.vokabeltraineronline.business.repository.IndexBoxRepository;
import de.heins.vokabeltraineronline.business.repository.QuestionWithAnswerRepository;
import de.heins.vokabeltraineronline.web.entities.IndexBoxes;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.QuestionWithAnswerAttrRef;


@Service
public class IndexBoxService {
	private static class DaysCounter {
		private int days;
		private void addDays(int days) {
			this.days = this.days + days;
		}
		private int getDays() {
			return days;
		}
		public String toString() {
			return String.valueOf(this.days);
		}
	}
	public static final IndexBoxAttrRef EMPTY_INDEX_BOX = new IndexBoxAttrRef();
	public static final Date LAST_SUCCESSSTEP_MADE_DATE = Calendar.getInstance().getTime();
	static {
		LAST_SUCCESSSTEP_MADE_DATE.setTime(253402297199999L);
	}
	@Autowired
	private IndexBoxRepository indexBoxRepository;
	@Autowired
	private QuestionWithAnswerRepository questionWithAnswerRepository;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private IndexBoxFactory indexBoxFactory;
	public IndexBoxes findAllForAppUser(SessionAppUser sessionAppUser) {
		List<AppUser> appUsers = appUserRepository.findByEmail(sessionAppUser.getEmail());
		if (appUsers.size() == 1) {
			AppUser appUser = appUsers.get(0);
			IndexBoxes indexBoxForms = new IndexBoxes();
			try {
				List<IndexBox> indexBoxes = indexBoxRepository.findByAppUser(appUser);
				indexBoxes.forEach(indexBox -> {
					IndexBoxAttrRef indexBoxForm = new IndexBoxAttrRef();
					indexBoxForm.setName(indexBox.getName());
					indexBoxForm.setSubject(indexBox.getSubject());
					List<QuestionWithAnswer> questionsWithAnsers = questionWithAnswerRepository.findByAppUserAndIndexBox(appUser, indexBox); 
					questionsWithAnsers.forEach(currentQwA -> {
						QuestionWithAnswerAttrRef questionWithAnswerAttrRef = new QuestionWithAnswerAttrRef();
						questionWithAnswerAttrRef.setQuestion(currentQwA.getQuestion());
						questionWithAnswerAttrRef.setAnswer(currentQwA.getAnswer());
						questionWithAnswerAttrRef.setIndexBoxDescription(//
								indexBox.getName()//
								+ QuestionWithAnswerService.INDEXBOX_DESCRIPTION_SPLITTER//
								+ indexBox.getSubject()//
						);
						String learningStrategyDescription = currentQwA.getLearningStrategy() != null//
								? currentQwA.getLearningStrategy().getName()//
								: "";
						questionWithAnswerAttrRef.setLearningStrategyDescription(learningStrategyDescription);
						String learningProgress = calculateProgress(currentQwA);
						questionWithAnswerAttrRef.setLearningProgress(learningProgress);
						indexBoxForm.getQuestionsWithAnswers().add(questionWithAnswerAttrRef);
					});
					indexBoxForm.setFilterOn(false);
					indexBoxForm.setLearningProgress(calculateProgress(indexBox));
					indexBoxForms.add(indexBoxForm);
				});
			} catch (Exception e) {
				System.out.println("Teststop");
				// this occurs only when there are no items in the database table,
				// or the table wasn't created yet.
				// nothing to do then.
			}
			return indexBoxForms;
		} else {
			throw new RuntimeException("No AppUser found or AppUser not unique by email");
		}
	}
	private String calculateProgress(IndexBox anIndexBox) {
		List<QuestionWithAnswer> questionWithAnswers = questionWithAnswerRepository.findByAppUserAndIndexBox(//
				anIndexBox.getAppUser()//
				, anIndexBox//
		);
		DaysCounter allDaysCounter = new DaysCounter();
		DaysCounter successDaysCounter = new DaysCounter();
		for (QuestionWithAnswer currentQuestionWithAnswer : questionWithAnswers) {
			countDays(currentQuestionWithAnswer, allDaysCounter, successDaysCounter);
		}
		int resultAsInt = (int) Math.round(100 * (float)successDaysCounter.getDays() / allDaysCounter.getDays());
		return resultAsInt+"%";
	}
	public IndexBoxAttrRef findForAppUserAndNameAndSubject(//
			SessionAppUser sessionAppUserForm//
			, String name//
			, String subject//
	) {
		AppUser appUser = appUserRepository.findByEmail(sessionAppUserForm.getEmail()).get(0);
		try {
			List<IndexBox> indexBoxes = indexBoxRepository.findByAppUserAndNameAndSubject(//
					appUser//
					, name//
					, subject//
			);
			if (indexBoxes.size()==1) {
				IndexBox indexBox = indexBoxes.get(0);
				IndexBoxAttrRef indexBoxAttrRef = new IndexBoxAttrRef();
				indexBoxAttrRef.setName(indexBox.getName());
				indexBoxAttrRef.setSubject(indexBox.getSubject());
				return indexBoxAttrRef;
				
			}
		} catch (Exception e) {
			// this occurs only when there are no items in the database table,
			// or the table wasn't created yet.
			// nothing to do then.
		}
		return  EMPTY_INDEX_BOX;
	}
	public void update(//
			SessionAppUser appUserForm//
			, IndexBoxAttrRef indexBoxAttrRef//
			, String oldName//
			, String oldSubject//
		) {
		AppUser appUser = appUserRepository.findByEmail(appUserForm.getEmail()).get(0);
		List<IndexBox> findByAppUserAndNameAndSubjectList = new ArrayList<IndexBox>();
		try {
			findByAppUserAndNameAndSubjectList//
				= indexBoxRepository.findByAppUserAndNameAndSubject(//
						appUser//
						, oldName//
						, oldSubject//
				);
		} catch (Exception e) {
			// this occurs only if there are no items in the database table,
			// or the table wasn't created yet.
			// nothing to do then.
		}
		IndexBox indexBox;
		if (findByAppUserAndNameAndSubjectList.isEmpty()) {//
			indexBox = indexBoxFactory//
					.setName(indexBoxAttrRef.getName())//
					.setSubject(indexBoxAttrRef.getSubject())
					.setAppUser(appUser)//
					.getNewObject();
				
		} else {
			indexBox = findByAppUserAndNameAndSubjectList.get(0); //
			indexBox.setName(indexBoxAttrRef.getName());//
			indexBox.setSubject(indexBoxAttrRef.getSubject());//
		}
		indexBoxRepository.save(indexBox);
	}
	private String calculateProgress(QuestionWithAnswer questionWithAnswer) {
		DaysCounter allDaysCounter = new DaysCounter();
		DaysCounter successDaysCounter = new DaysCounter();
		countDays(questionWithAnswer, allDaysCounter, successDaysCounter);
		int resultAsFloat = (int) Math.round(100 * (float)successDaysCounter.getDays() / allDaysCounter.getDays());
		return resultAsFloat+"%";
	}
	private void countDays(//
			QuestionWithAnswer questionWithAnswer//
			, DaysCounter allDaysCounter//
			, DaysCounter successDaysCounter//
	) {
		SuccessStep actualSuccessStep = questionWithAnswer.getActualSuccessStep();
		List<SuccessStep> successSteps = questionWithAnswer
				.getLearningStrategy()//
				.getSuccessSteps();
		if (successSteps.isEmpty()) {
			allDaysCounter.addDays(1);
			if (questionWithAnswer.getNextAppearance().equals(LAST_SUCCESSSTEP_MADE_DATE)) {
				successDaysCounter.addDays(1);
			}
		} else {
			boolean actualSuccessStepReached = false;
			for (SuccessStep currentSuccessStep : successSteps) {
				// not only the days are counted, but the successSteps too.
				// take one day extra for each successStep.
				allDaysCounter.addDays(1 + currentSuccessStep.getNextAppearanceInDays());
				if (//
						actualSuccessStep != null//
						&& !currentSuccessStep.equals(actualSuccessStep)//
						&& !actualSuccessStepReached//
				) {
					successDaysCounter.addDays(1 + currentSuccessStep.getNextAppearanceInDays());
				} else {
					actualSuccessStepReached = true;
					if (questionWithAnswer.getNextAppearance().equals(LAST_SUCCESSSTEP_MADE_DATE)) {
						successDaysCounter.addDays(1 + currentSuccessStep.getNextAppearanceInDays());
					}
				}
			}
		}
	}

}
