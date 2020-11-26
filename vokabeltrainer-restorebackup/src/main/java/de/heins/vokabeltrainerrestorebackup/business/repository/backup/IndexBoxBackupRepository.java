package de.heins.vokabeltrainerrestorebackup.business.repository.backup;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerrestorebackup.business.entity.backup.IndexBoxBackup;


@Repository
public interface IndexBoxBackupRepository extends CrudRepository<IndexBoxBackup, Long>{

	@Query(value = "SELECT MAX(id) FROM IndexBoxBackup")
	public List<Long> getMaxId();

}
