package de.heins.vokabeltrainerbackup.business.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerbackup.business.entity.QuestionWithAnswer;

@Repository
public interface QuestionWithAnswerRepository extends CrudRepository<QuestionWithAnswer, Long>{

	@Query(value = "SELECT MAX(id) FROM QuestionWithAnswer")
	public Optional<Long> getMaxId();

}
