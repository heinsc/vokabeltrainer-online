package de.heins.vokabeltrainerdropmaindatabase.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerdropmaindatabase.entity.QuestionWithAnswer;

@Repository
public interface QuestionWithAnswerRepository extends CrudRepository<QuestionWithAnswer, Long> {
}
