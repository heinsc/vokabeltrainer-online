package de.heins.vokabeltrainerbackup;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltrainerbackup.business.entity.SuccessStep;
import de.heins.vokabeltrainerbackup.business.entity.backup.AppUserBackup;
import de.heins.vokabeltrainerbackup.business.entity.backup.BehaviourIfWrongBackup;
import de.heins.vokabeltrainerbackup.business.entity.backup.SuccessStepBackup;
import de.heins.vokabeltrainerbackup.business.repository.SuccessStepRepository;
import de.heins.vokabeltrainerbackup.business.repository.backup.SuccessStepBackupRepository;

@Service
public class VokabeltrainerBackupSuccessStepHandler
	extends VokabeltrainerBackupTableHandler<SuccessStep, SuccessStepBackup> {

	@Autowired
	private SuccessStepRepository sourceRepository;
	@Autowired
	private SuccessStepBackupRepository targetRepository;
	@Autowired
	private VokabeltrainerBackupAppUserHandler appUserTableHandler;

	@Override
	public SuccessStepBackup transferAndSaveSingleEntry(SuccessStep sourceEntity) {
		AppUserBackup appUserTarget = appUserTableHandler.transferAndSaveSingleEntry(sourceEntity.getAppUser());
		List<SuccessStepBackup> resultSet = targetRepository.findByAppUserBackupAndName(//
				appUserTarget//
				, sourceEntity.getName()//
		);
		if (!resultSet.isEmpty()) {
			return resultSet.get(0);
		}
		SuccessStepBackup targetEntity = new SuccessStepBackup(//
						null//
						, sourceEntity.getName()//
						, sourceEntity.getNextAppearanceInDays()//
						, BehaviourIfWrongBackup.valueOf(//
								sourceEntity.getBehaviourIfWrong().name()//
						)
						, appUserTarget
				);

		return targetRepository.save(targetEntity);
	}

	@Override
	protected Optional<SuccessStep> findById(long id) {
		return sourceRepository.findById(id);
	}

	@Override
	protected List<Long> getMaxId() {
		return sourceRepository.getMaxId();
	}

}
