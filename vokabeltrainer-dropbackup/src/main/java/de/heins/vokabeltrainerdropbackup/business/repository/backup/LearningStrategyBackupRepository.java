package de.heins.vokabeltrainerdropbackup.business.repository.backup;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerdropbackup.business.entity.backup.LearningStrategyBackup;

@Repository
public interface LearningStrategyBackupRepository extends CrudRepository<LearningStrategyBackup, Long>{
}
