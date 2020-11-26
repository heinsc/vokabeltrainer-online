package de.heins.vokabeltrainerbackup.business.repository.backup;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerbackup.business.entity.backup.AppUserBackup;
import de.heins.vokabeltrainerbackup.business.entity.backup.IndexBoxBackup;

@Repository
public interface IndexBoxBackupRepository extends CrudRepository<IndexBoxBackup, Long>{
	public List<IndexBoxBackup> findByAppUserBackupAndNameAndSubject(//
			AppUserBackup appUserBackup//
			, String name//
			, String subject//
	);
	
}
