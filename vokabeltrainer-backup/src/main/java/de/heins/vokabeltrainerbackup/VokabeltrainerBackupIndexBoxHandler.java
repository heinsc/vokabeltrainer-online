package de.heins.vokabeltrainerbackup;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltrainerbackup.business.entity.IndexBox;
import de.heins.vokabeltrainerbackup.business.entity.backup.AppUserBackup;
import de.heins.vokabeltrainerbackup.business.entity.backup.IndexBoxBackup;
import de.heins.vokabeltrainerbackup.business.repository.IndexBoxRepository;
import de.heins.vokabeltrainerbackup.business.repository.backup.IndexBoxBackupRepository;

@Service
public class VokabeltrainerBackupIndexBoxHandler
		extends VokabeltrainerBackupTableHandler<IndexBox, IndexBoxBackup> {

	@Autowired
	IndexBoxRepository sourceRepository;
	@Autowired
	private IndexBoxBackupRepository targetRepository;
	@Autowired
	private VokabeltrainerBackupAppUserHandler vokabeltrainerBackupAppUserHandler;

	@Override
	public IndexBoxBackup transferAndSaveSingleEntry(IndexBox sourceEntity) {
		
		AppUserBackup appUserTarget = vokabeltrainerBackupAppUserHandler.transferAndSaveSingleEntry(sourceEntity.getAppUser());
		List<IndexBoxBackup> resultSet = targetRepository.findByAppUserBackupAndNameAndSubject(//
				appUserTarget//
				, sourceEntity.getName()//
				, sourceEntity.getSubject()//
		);
		if (!resultSet.isEmpty()) {
			return resultSet.get(0);
		}
		IndexBoxBackup targetEntity= new IndexBoxBackup(//
				null//
				, sourceEntity.getName()//
				, sourceEntity.getSubject()//
				, appUserTarget
		);
		targetEntity.setActualInUse(sourceEntity.isActualInUse());
		return targetRepository.save(targetEntity);
				
	}

	@Override
	protected Optional<IndexBox> findById(long id) {
		return sourceRepository.findById(id);
	}

	@Override
	protected Optional<Long> getMaxId() {
		return sourceRepository.getMaxId();
	}

}
