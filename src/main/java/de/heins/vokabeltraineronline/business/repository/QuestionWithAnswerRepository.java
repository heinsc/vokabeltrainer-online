package de.heins.vokabeltraineronline.business.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltraineronline.business.entity.QuestionWithAnswer;
import de.heins.vokabeltraineronline.business.entity.User;

@Repository
public interface QuestionWithAnswerRepository extends CrudRepository<QuestionWithAnswer, Long>{
	public List<QuestionWithAnswer> findByUser(User user);
	//noch mal rausfinden, wie man eine BeforeDate-Suche umsetzt.
	public List<QuestionWithAnswer> findByUserAndAfterNextAppearance(User user, Date nextAppearance);

}
