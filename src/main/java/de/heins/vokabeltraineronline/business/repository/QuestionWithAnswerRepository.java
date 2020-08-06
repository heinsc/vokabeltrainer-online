package de.heins.vokabeltraineronline.business.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltraineronline.business.entity.QuestionWithAnswer;
import de.heins.vokabeltraineronline.business.entity.User;

@Repository
public interface QuestionWithAnswerRepository extends CrudRepository<QuestionWithAnswer, Long> {
	public List<QuestionWithAnswer> findByUser(User user);

//	// noch mal rausfinden, wie man eine BeforeDate-Suche umsetzt.
//	@Query("select questionWithAnswer from QuestionWithAnser questionWithAnswe where questionWithAnswer.nextAppearance <= :today and questionWithAnswer.user = :user")
//	Set<QuestionWithAnswer> findAllByUserWithNextAppearanceBeforeToday(@Param("today") Date today,
//			@Param("user") User user);
	public Set<QuestionWithAnswer> findByNextAppearanceLessThanEqualAndUser(Date today, User user);
}
