package de.heins.vokabeltrainerrestorebackup.business.repository.backup;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerrestorebackup.business.entity.backup.QuestionWithAnswerBackup;

@Repository
public interface QuestionWithAnswerBackupRepository extends CrudRepository<QuestionWithAnswerBackup, Long>{

	@Query(value = "SELECT MAX(id) FROM QuestionWithAnswerBackup")
	public Optional<Long> getMaxId();

}
