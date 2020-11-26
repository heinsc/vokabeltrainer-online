package de.heins.vokabeltrainerbackup;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltrainerbackup.business.entity.QuestionWithAnswer;
import de.heins.vokabeltrainerbackup.business.entity.backup.AppUserBackup;
import de.heins.vokabeltrainerbackup.business.entity.backup.QuestionWithAnswerBackup;
import de.heins.vokabeltrainerbackup.business.repository.QuestionWithAnswerRepository;
import de.heins.vokabeltrainerbackup.business.repository.backup.QuestionWithAnswerBackupRepository;

@Service
public class VokabeltrainerBackupQuestionWithAnswerHandler
		extends VokabeltrainerBackupTableHandler<QuestionWithAnswer, QuestionWithAnswerBackup> {

	@Autowired
	private QuestionWithAnswerRepository sourceRepository;
	@Autowired
	private QuestionWithAnswerBackupRepository targetRepository;
	@Autowired
	private VokabeltrainerBackupAppUserHandler appUserTableHandler;
	@Autowired
	private VokabeltrainerBackupIndexBoxHandler indexBoxTableHandler;
	@Autowired
	private VokabeltrainerBackupLearningStrategyHandler learningStrategyTableHandler;
	@Autowired
	private VokabeltrainerBackupSuccessStepHandler successStepTableHandler;

	@Override
	public QuestionWithAnswerBackup transferAndSaveSingleEntry(QuestionWithAnswer sourceEntity) {
		AppUserBackup appUserTarget = appUserTableHandler.transferAndSaveSingleEntry(sourceEntity.getAppUser());
		List<QuestionWithAnswerBackup> resultSet = targetRepository.findByAppUserBackupAndQuestion(//
				appUserTarget//
				, sourceEntity.getQuestion()//
		);
		if (!resultSet.isEmpty()) {
			return resultSet.get(0);
		}
		QuestionWithAnswerBackup targetEntity = new QuestionWithAnswerBackup(//
						sourceEntity.getId()//
						, indexBoxTableHandler.transferAndSaveSingleEntry(//
								sourceEntity.getIndexBox()//
						)
						, learningStrategyTableHandler.transferAndSaveSingleEntry(//
								sourceEntity.getLearningStrategy()//
						)
						, sourceEntity.getQuestion()//
						, sourceEntity.getAnswer()//
						, appUserTarget
				);
		targetEntity.setActualSuccessStepBackup(//
				null != sourceEntity.getActualSuccessStep()
				? successStepTableHandler.transferAndSaveSingleEntry(//
						sourceEntity.getActualSuccessStep()//
				)//
				: null
		);
		
		targetEntity.setNextAppearance(sourceEntity.getNextAppearance());

		return targetRepository.save(targetEntity);
	}

	@Override
	protected Optional<QuestionWithAnswer> findById(long id) {
		return sourceRepository.findById(id);
	}

	@Override
	protected List<Long> getMaxId() {
		return sourceRepository.getMaxId();
	}

	
	
}
