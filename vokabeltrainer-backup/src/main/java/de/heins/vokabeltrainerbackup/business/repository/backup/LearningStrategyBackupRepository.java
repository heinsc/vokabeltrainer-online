package de.heins.vokabeltrainerbackup.business.repository.backup;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerbackup.business.entity.backup.AppUserBackup;
import de.heins.vokabeltrainerbackup.business.entity.backup.LearningStrategyBackup;

@Repository
public interface LearningStrategyBackupRepository extends CrudRepository<LearningStrategyBackup, Long>{
	public List<LearningStrategyBackup> findByAppUserBackupAndName(AppUserBackup appUserBackup, String oldVersionOfLearningStrategyName);
}
