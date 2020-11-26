package de.heins.vokabeltrainerdropbackup.business.repository.backup;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerdropbackup.business.entity.backup.SuccessStepBackup;

@Repository
public interface SuccessStepBackupRepository extends CrudRepository<SuccessStepBackup, Long>{

}
