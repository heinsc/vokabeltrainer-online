package de.heins.vokabeltrainerbackup.business.repository.backup;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerbackup.business.entity.backup.AppUserBackup;
import de.heins.vokabeltrainerbackup.business.entity.backup.SuccessStepBackup;

@Repository
public interface SuccessStepBackupRepository extends CrudRepository<SuccessStepBackup, Long>{
	public List<SuccessStepBackup> findByAppUserBackupAndName(AppUserBackup appUserBackup, String name);
}
