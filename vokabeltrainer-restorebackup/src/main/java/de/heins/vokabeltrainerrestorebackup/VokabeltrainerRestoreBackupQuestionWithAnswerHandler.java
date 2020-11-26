package de.heins.vokabeltrainerrestorebackup;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltrainerrestorebackup.business.entity.AppUser;
import de.heins.vokabeltrainerrestorebackup.business.entity.QuestionWithAnswer;
import de.heins.vokabeltrainerrestorebackup.business.entity.backup.QuestionWithAnswerBackup;
import de.heins.vokabeltrainerrestorebackup.business.repository.QuestionWithAnswerRepository;
import de.heins.vokabeltrainerrestorebackup.business.repository.backup.QuestionWithAnswerBackupRepository;

@Service
public class VokabeltrainerRestoreBackupQuestionWithAnswerHandler
		extends VokabeltrainerRestoreBackupTableHandler<QuestionWithAnswerBackup, QuestionWithAnswer> {
	
	@Autowired
	private QuestionWithAnswerBackupRepository sourceRepository;
	@Autowired
	private QuestionWithAnswerRepository targetRepository;
	@Autowired
	private VokabeltrainerRestoreBackupAppUserHandler appUserTableHandler;
	@Autowired
	private VokabeltrainerRestoreBackupIndexBoxHandler indexBoxTableHandler;
	@Autowired
	private VokabeltrainerRestoreBackupLearningStrategyHandler learningStrategyTableHandler;
	@Autowired
	private VokabeltrainerRestoreBackupSuccessStepHandler successStepTableHandler;

	@Override
	public QuestionWithAnswer transferAndSaveSingleEntry(QuestionWithAnswerBackup sourceEntity) {
		AppUser appUserTarget = appUserTableHandler.transferAndSaveSingleEntry(sourceEntity.getAppUserBackup());
		List<QuestionWithAnswer> resultSet = targetRepository.findByAppUserAndQuestion(//
				appUserTarget//
				, sourceEntity.getQuestion()//
		);
		if (!resultSet.isEmpty()) {
			return resultSet.get(0);
		}
		QuestionWithAnswer targetEntity = new QuestionWithAnswer(//
						sourceEntity.getId()//
						, indexBoxTableHandler.transferAndSaveSingleEntry(//
								sourceEntity.getIndexBoxBackup()//
						)
						, learningStrategyTableHandler.transferAndSaveSingleEntry(//
								sourceEntity.getLearningStrategyBackup()//
						)
						, sourceEntity.getQuestion()//
						, sourceEntity.getAnswer()//
						, appUserTarget
				);
		targetEntity.setActualSuccessStep(//
				null != sourceEntity.getActualSuccessStepBackup()
				? successStepTableHandler.transferAndSaveSingleEntry(//
						sourceEntity.getActualSuccessStepBackup()//
				)//
				: null
		);
		
		targetEntity.setNextAppearance(sourceEntity.getNextAppearance());

		return targetRepository.save(targetEntity);
	}

	@Override
	protected Optional<QuestionWithAnswerBackup> findById(long id) {
		return sourceRepository.findById(id);
	}

	@Override
	protected List<Long> getMaxId() {
		return sourceRepository.getMaxId();
	}

}
