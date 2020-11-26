package de.heins.vokabeltrainerrestorebackup.business.repository.backup;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerrestorebackup.business.entity.backup.AppUserBackup;

@Repository
public interface AppUserBackupRepository extends CrudRepository<AppUserBackup, Long>{

	@Query(value = "SELECT MAX(id) FROM AppUserBackup")
	public List<Long> getMaxId();

}
