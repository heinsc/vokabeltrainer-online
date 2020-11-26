package de.heins.vokabeltrainerrestorebackup;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltrainerrestorebackup.business.entity.AppUser;
import de.heins.vokabeltrainerrestorebackup.business.entity.BehaviourIfWrong;
import de.heins.vokabeltrainerrestorebackup.business.entity.SuccessStep;
import de.heins.vokabeltrainerrestorebackup.business.entity.backup.SuccessStepBackup;
import de.heins.vokabeltrainerrestorebackup.business.repository.SuccessStepRepository;
import de.heins.vokabeltrainerrestorebackup.business.repository.backup.SuccessStepBackupRepository;

@Service
public class VokabeltrainerRestoreBackupSuccessStepHandler
		extends VokabeltrainerRestoreBackupTableHandler<SuccessStepBackup, SuccessStep> {
	
	@Autowired
	private SuccessStepBackupRepository sourceRepository;
	@Autowired
	private SuccessStepRepository targetRepository;
	@Autowired
	private VokabeltrainerRestoreBackupAppUserHandler appUserTableHandler;

	@Override
	public SuccessStep transferAndSaveSingleEntry(SuccessStepBackup sourceEntity) {
		AppUser appUserTarget = appUserTableHandler.transferAndSaveSingleEntry(sourceEntity.getAppUserBackup());
		List<SuccessStep> resultSet = targetRepository.findByAppUserAndName(//
				appUserTarget//
				, sourceEntity.getName()//
		);
		if (!resultSet.isEmpty()) {
			return resultSet.get(0);
		}
		SuccessStep targetEntity = new SuccessStep(//
						null//
						, sourceEntity.getName()//
						, sourceEntity.getNextAppearanceInDays()//
						, BehaviourIfWrong.valueOf(//
								sourceEntity.getBehaviourIfWrongBackup().name()//
						)
						, appUserTarget
				);

		return targetRepository.save(targetEntity);
	}

	@Override
	protected Optional<SuccessStepBackup> findById(long id) {
		return sourceRepository.findById(id);
	}

	@Override
	protected List<Long> getMaxId() {
		return sourceRepository.getMaxId();
	}

}
