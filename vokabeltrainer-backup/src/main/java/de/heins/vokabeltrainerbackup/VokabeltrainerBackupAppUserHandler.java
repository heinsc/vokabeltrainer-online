package de.heins.vokabeltrainerbackup;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltrainerbackup.business.entity.AppUser;
import de.heins.vokabeltrainerbackup.business.entity.backup.AppUserBackup;
import de.heins.vokabeltrainerbackup.business.entity.backup.BehaviourIfPoolWithWrongAnswersIsFullBackup;
import de.heins.vokabeltrainerbackup.business.entity.backup.FaultToleranceBackup;
import de.heins.vokabeltrainerbackup.business.repository.AppUserRepository;
import de.heins.vokabeltrainerbackup.business.repository.backup.AppUserBackupRepository;

@Service
public class VokabeltrainerBackupAppUserHandler
		extends VokabeltrainerBackupTableHandler<AppUser, AppUserBackup> {

	@Autowired
	private AppUserRepository sourceRepository;
	@Autowired
	private AppUserBackupRepository targetRepository;

	@Override
	public AppUserBackup transferAndSaveSingleEntry(AppUser sourceEntity) {
		List<AppUserBackup> resultSet = targetRepository.findByEmail(sourceEntity.getEmail());
		if (!resultSet.isEmpty()) {
			return resultSet.get(0);
		}
		AppUserBackup targetEntity = new AppUserBackup(//
				null//
				, sourceEntity.getEmail()//
				, sourceEntity.getPassword()//
				, FaultToleranceBackup.valueOf(//
						sourceEntity.getFaultTolerance().name()//
				), sourceEntity.getMaxNumberOfWrongAnswersPerSession()//
				, BehaviourIfPoolWithWrongAnswersIsFullBackup.valueOf(//
						sourceEntity.getBehaviourIfPoolWithWrongAnswersIsFull().name()//
				), sourceEntity.getLastLogin()//
		);
		targetEntity.setBehaviourIfPoolWithWrongAnswersIsFullBackup(BehaviourIfPoolWithWrongAnswersIsFullBackup.valueOf(//
						sourceEntity.getBehaviourIfPoolWithWrongAnswersIsFull().name()//
					));
		
		return targetRepository.save(targetEntity);
	}

	@Override
	protected Optional<AppUser> findById(long id) {
		return sourceRepository.findById(id);
	}

	@Override
	protected List<Long> getMaxId() {
		return sourceRepository.getMaxId();
	}

}
