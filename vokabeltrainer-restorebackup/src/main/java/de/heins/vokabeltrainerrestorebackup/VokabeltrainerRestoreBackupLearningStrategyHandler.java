package de.heins.vokabeltrainerrestorebackup;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltrainerrestorebackup.business.entity.AppUser;
import de.heins.vokabeltrainerrestorebackup.business.entity.LearningStrategy;
import de.heins.vokabeltrainerrestorebackup.business.entity.SuccessStep;
import de.heins.vokabeltrainerrestorebackup.business.entity.backup.LearningStrategyBackup;
import de.heins.vokabeltrainerrestorebackup.business.entity.backup.SuccessStepBackup;
import de.heins.vokabeltrainerrestorebackup.business.repository.LearningStrategyRepository;
import de.heins.vokabeltrainerrestorebackup.business.repository.backup.LearningStrategyBackupRepository;

@Service
public class VokabeltrainerRestoreBackupLearningStrategyHandler
		extends VokabeltrainerRestoreBackupTableHandler<LearningStrategyBackup, LearningStrategy> {
	
	@Autowired
	LearningStrategyBackupRepository sourceRepository;
	@Autowired
	LearningStrategyRepository targetRepository;
	@Autowired
	VokabeltrainerRestoreBackupAppUserHandler appUserTableHandler;
	@Autowired
	VokabeltrainerRestoreBackupSuccessStepHandler successStepTableHandler;

	@Override
	public LearningStrategy transferAndSaveSingleEntry(LearningStrategyBackup sourceEntity) {
		AppUser appUserTarget = appUserTableHandler.transferAndSaveSingleEntry(sourceEntity.getAppUserBackup());
		List<LearningStrategy> resultSet = targetRepository.findByAppUserAndName(//
				appUserTarget//
				, sourceEntity.getName()//
		);
		
		if (!resultSet.isEmpty()) {
			return resultSet.get(0);
		}
		LearningStrategy targetEntity = new LearningStrategy(//
					null//
					, sourceEntity.getName()//
					, appUserTarget
				);
		List<SuccessStepBackup> successSteps = sourceEntity.getSuccessStepBackups();
		for (SuccessStepBackup currentSuccessStep : successSteps) {
			SuccessStep targetSuccessStep = successStepTableHandler.transferAndSaveSingleEntry(currentSuccessStep);
			targetEntity.addSuccessStep(targetSuccessStep);
		}
		return targetRepository.save(targetEntity);
	}

	@Override
	protected Optional<LearningStrategyBackup> findById(long id) {
		return sourceRepository.findById(id);
	}

	@Override
	protected List<Long> getMaxId() {
		return sourceRepository.getMaxId();
	}

}
