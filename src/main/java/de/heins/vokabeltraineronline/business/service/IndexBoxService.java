package de.heins.vokabeltraineronline.business.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltraineronline.business.repository.IndexBoxRepository;
import de.heins.vokabeltraineronline.business.repository.AppUserRepository;
import de.heins.vokabeltraineronline.business.entity.IndexBox;
import de.heins.vokabeltraineronline.business.entity.IndexBoxFactory;
import de.heins.vokabeltraineronline.business.entity.QuestionWithAnswer;
import de.heins.vokabeltraineronline.business.entity.AppUser;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.QuestionWithAnswerAttrRef;


@Service
public class IndexBoxService {
	public static final IndexBoxAttrRef EMPTY_INDEX_BOX = new IndexBoxAttrRef();
	@Autowired
	private IndexBoxRepository indexBoxRepository;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private IndexBoxFactory indexBoxFactory;
	public List<IndexBoxAttrRef> findAllForAppUser(SessionAppUser sessionAppUser) {
		List<AppUser> appUsers = appUserRepository.findByEmail(sessionAppUser.getEmail());
		if (appUsers.size() == 1) {
			AppUser appUser = appUsers.get(0);
			List<IndexBoxAttrRef> indexBoxForms = new ArrayList<IndexBoxAttrRef>();
			try {
				List<IndexBox> indexBoxes = indexBoxRepository.findByAppUser(appUser);
				indexBoxes.forEach(indexBox -> {
					IndexBoxAttrRef indexBoxForm = new IndexBoxAttrRef();
					indexBoxForm.setName(indexBox.getName());
					indexBoxForm.setSubject(indexBox.getSubject());
					Set<QuestionWithAnswer> questionsWithAnsers = indexBox.getQuestionsWithAnsers();
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
						String successStepDescription = currentQwA.getActualSuccessStep() != null//
								? currentQwA.getActualSuccessStep().getName()//
								: "not yet started or already finished";
						questionWithAnswerAttrRef.setActualSuccessStepDescription(successStepDescription);
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
