package de.heins.vokabeltrainerbackup.business.repository.backup;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerbackup.business.entity.backup.AppUserBackup;
import de.heins.vokabeltrainerbackup.business.entity.backup.QuestionWithAnswerBackup;

@Repository
public interface QuestionWithAnswerBackupRepository extends CrudRepository<QuestionWithAnswerBackup, Long> {
	public List<QuestionWithAnswerBackup> findByAppUserBackupAndQuestion(AppUserBackup appUserBackup, String question);

}
