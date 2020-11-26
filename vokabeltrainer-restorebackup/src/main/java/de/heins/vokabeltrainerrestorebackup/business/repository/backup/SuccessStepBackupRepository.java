package de.heins.vokabeltrainerrestorebackup.business.repository.backup;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerrestorebackup.business.entity.backup.SuccessStepBackup;

@Repository
public interface SuccessStepBackupRepository extends CrudRepository<SuccessStepBackup, Long>{

	@Query(value = "SELECT MAX(id) FROM SuccessStepBackup")
	public Optional<Long> getMaxId();

}
