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
import de.heins.vokabeltraineronline.business.entity.LearningStrategy;
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
					indexBoxForm.setFilterOn(true);
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
	private String calculateProgress(QuestionWithAnswer questionWithAnswer) {
		SuccessStep actualSuccessStep = questionWithAnswer.getActualSuccessStep();
		LearningStrategy learningStrategy = questionWithAnswer.getLearningStrategy();
		List<SuccessStep> successSteps = learningStrategy.getSuccessSteps();
		int totalDaysOfLearningStrategy=0;
		int sumUntilActualSuccessStep=0;
		if (questionWithAnswer.getActualSuccessStep() == null) {
			if (questionWithAnswer.getNextAppearance().equals(LAST_SUCCESSSTEP_MADE_DATE)) {
				return "100 %";
			} else {
				return "0 %";
			}
		}
		boolean actualSuccessStepFound = false;
		for (SuccessStep currentSuccessStep : successSteps) {
			totalDaysOfLearningStrategy = totalDaysOfLearningStrategy + currentSuccessStep.getNextAppearanceInDays();
			if (currentSuccessStep.equals(actualSuccessStep)) {
				actualSuccessStepFound = true;
			}
			if (!actualSuccessStepFound) {
				sumUntilActualSuccessStep = sumUntilActualSuccessStep + currentSuccessStep.getNextAppearanceInDays();
			}				
				
		}
		int resultAsFloat = (int) Math.round(100 * (float)sumUntilActualSuccessStep / totalDaysOfLearningStrategy);
		return resultAsFloat+"%";
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

}
