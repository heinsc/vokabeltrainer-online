package de.heins.vokabeltrainerrestorebackup.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerrestorebackup.business.entity.AppUser;
import de.heins.vokabeltrainerrestorebackup.business.entity.QuestionWithAnswer;

@Repository
public interface QuestionWithAnswerRepository extends CrudRepository<QuestionWithAnswer, Long> {
	public List<QuestionWithAnswer> findByAppUserAndQuestion(AppUser appUser, String question);

}
