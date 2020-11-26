package de.heins.vokabeltrainerrestorebackup;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltrainerrestorebackup.business.entity.AppUser;
import de.heins.vokabeltrainerrestorebackup.business.entity.BehaviourIfPoolWithWrongAnswersIsFull;
import de.heins.vokabeltrainerrestorebackup.business.entity.FaultTolerance;
import de.heins.vokabeltrainerrestorebackup.business.entity.backup.AppUserBackup;
import de.heins.vokabeltrainerrestorebackup.business.repository.AppUserRepository;
import de.heins.vokabeltrainerrestorebackup.business.repository.backup.AppUserBackupRepository;

@Service
public class VokabeltrainerRestoreBackupAppUserHandler
		extends VokabeltrainerRestoreBackupTableHandler<AppUserBackup, AppUser> {
	
	@Autowired
	AppUserBackupRepository sourceRepository;
	@Autowired
	AppUserRepository targetRepository;

	@Override
	public AppUser transferAndSaveSingleEntry(AppUserBackup sourceEntity) {
		List<AppUser> resultSet = targetRepository.findByEmail(sourceEntity.getEmail());
		if (!resultSet.isEmpty()) {
			return resultSet.get(0);
		}
		AppUser targetEntity = new AppUser(//
				null//
				, sourceEntity.getEmail()//
				, sourceEntity.getPassword()//
				, FaultTolerance.valueOf(//
						sourceEntity.getFaultToleranceBackup().name()//
				), sourceEntity.getMaxNumberOfWrongAnswersPerSession()//
				, BehaviourIfPoolWithWrongAnswersIsFull.valueOf(//
						sourceEntity.getBehaviourIfPoolWithWrongAnswersIsFullBackup().name()//
				), sourceEntity.getLastLogin()//
		);
		targetEntity.setBehaviourIfPoolWithWrongAnswersIsFull(BehaviourIfPoolWithWrongAnswersIsFull.valueOf(//
				sourceEntity.getBehaviourIfPoolWithWrongAnswersIsFullBackup().name()//
					));
		
		return targetRepository.save(targetEntity);
	}

	@Override
	protected Optional<AppUserBackup> findById(long id) {
		return sourceRepository.findById(id);
	}

	@Override
	protected List<Long> getMaxId() {
		return sourceRepository.getMaxId();
	}

}
