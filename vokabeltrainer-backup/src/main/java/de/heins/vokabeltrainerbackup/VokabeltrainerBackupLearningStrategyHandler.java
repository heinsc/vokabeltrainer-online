package de.heins.vokabeltrainerbackup;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltrainerbackup.business.entity.LearningStrategy;
import de.heins.vokabeltrainerbackup.business.entity.SuccessStep;
import de.heins.vokabeltrainerbackup.business.entity.backup.AppUserBackup;
import de.heins.vokabeltrainerbackup.business.entity.backup.LearningStrategyBackup;
import de.heins.vokabeltrainerbackup.business.entity.backup.SuccessStepBackup;
import de.heins.vokabeltrainerbackup.business.repository.LearningStrategyRepository;
import de.heins.vokabeltrainerbackup.business.repository.backup.LearningStrategyBackupRepository;

@Service
public class VokabeltrainerBackupLearningStrategyHandler
		extends VokabeltrainerBackupTableHandler<LearningStrategy, LearningStrategyBackup> {

	@Autowired
	private LearningStrategyRepository sourceRepository;
	@Autowired
	private LearningStrategyBackupRepository targetRepository;
	@Autowired
	private VokabeltrainerBackupAppUserHandler appUserTableHandler;
	@Autowired
	private VokabeltrainerBackupSuccessStepHandler successStepTableHandler;

	@Override
	public LearningStrategyBackup transferAndSaveSingleEntry(LearningStrategy sourceEntity) {
		AppUserBackup appUserTarget = appUserTableHandler.transferAndSaveSingleEntry(sourceEntity.getAppUser());
		List<LearningStrategyBackup> resultSet = targetRepository.findByAppUserBackupAndName(//
				appUserTarget//
				, sourceEntity.getName()//
		);
		
		if (!resultSet.isEmpty()) {
			return resultSet.get(0);
		}
		LearningStrategyBackup targetEntity = new LearningStrategyBackup(//
					null//
					, sourceEntity.getName()//
					, appUserTarget
				);
		List<SuccessStep> successSteps = sourceEntity.getSuccessSteps();
		for (SuccessStep currentSuccessStep : successSteps) {
			SuccessStepBackup targetSuccessStep = successStepTableHandler.transferAndSaveSingleEntry(currentSuccessStep);
			targetEntity.addSuccessStepBackup(targetSuccessStep);
		}
		return targetRepository.save(targetEntity);
	}

	@Override
	protected Optional<LearningStrategy> findById(long id) {
		return sourceRepository.findById(id);
	}

	@Override
	protected Optional<Long> getMaxId() {
		return sourceRepository.getMaxId();
	}

}
