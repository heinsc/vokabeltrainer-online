package de.heins.vokabeltrainerrestorebackup.business.repository.backup;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerrestorebackup.business.entity.backup.LearningStrategyBackup;

@Repository
public interface LearningStrategyBackupRepository extends CrudRepository<LearningStrategyBackup, Long>{

	@Query(value = "SELECT MAX(id) FROM LearningStrategyBackup")
	public List<Long> getMaxId();

}
