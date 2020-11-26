package de.heins.vokabeltrainerbackup.business.repository.backup;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerbackup.business.entity.backup.AppUserBackup;

@Repository
public interface AppUserBackupRepository extends CrudRepository<AppUserBackup, Long>{

	List<AppUserBackup> findByEmail(String email);
	

}
