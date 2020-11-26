package de.heins.vokabeltrainerrestorebackup;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltrainerrestorebackup.business.entity.AppUser;
import de.heins.vokabeltrainerrestorebackup.business.entity.IndexBox;
import de.heins.vokabeltrainerrestorebackup.business.entity.backup.IndexBoxBackup;
import de.heins.vokabeltrainerrestorebackup.business.repository.IndexBoxRepository;
import de.heins.vokabeltrainerrestorebackup.business.repository.backup.IndexBoxBackupRepository;

@Service
public class VokabeltrainerRestoreBackupIndexBoxHandler
		extends VokabeltrainerRestoreBackupTableHandler<IndexBoxBackup, IndexBox> {
	
	@Autowired
	IndexBoxBackupRepository sourceRepository;
	@Autowired
	IndexBoxRepository targetRepository;
	@Autowired
	VokabeltrainerRestoreBackupAppUserHandler vokabeltrainerRestoreBackupAppUserHandler;

	@Override
	public IndexBox transferAndSaveSingleEntry(IndexBoxBackup sourceEntity) {
		
		AppUser appUserTarget = vokabeltrainerRestoreBackupAppUserHandler.transferAndSaveSingleEntry(sourceEntity.getAppUserBackup());
		List<IndexBox> resultSet = targetRepository.findByAppUserAndNameAndSubject(//
				appUserTarget//
				, sourceEntity.getName()//
				, sourceEntity.getSubject()//
		);
		if (!resultSet.isEmpty()) {
			return resultSet.get(0);
		}
		IndexBox targetEntity = new IndexBox(//
				null//
				, sourceEntity.getName()//
				, sourceEntity.getSubject()//
				, appUserTarget
		);
		targetEntity.setActualInUse(sourceEntity.isActualInUse());
		return targetRepository.save(targetEntity);
				
	}

	@Override
	protected Optional<IndexBoxBackup> findById(long id) {
		return sourceRepository.findById(id);
	}

	@Override
	protected List<Long> getMaxId() {
		return sourceRepository.getMaxId();
	}

}
