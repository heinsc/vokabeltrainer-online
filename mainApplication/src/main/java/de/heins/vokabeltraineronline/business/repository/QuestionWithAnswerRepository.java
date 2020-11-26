package de.heins.vokabeltraineronline.business.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltraineronline.business.entity.QuestionWithAnswer;
import de.heins.vokabeltraineronline.business.entity.AppUser;
import de.heins.vokabeltraineronline.business.entity.IndexBox;

@Repository
public interface QuestionWithAnswerRepository extends CrudRepository<QuestionWithAnswer, Long> {
	public List<QuestionWithAnswer> findByAppUser(AppUser appUser);

//	// noch mal rausfinden, wie man eine BeforeDate-Suche umsetzt.
//	@Query("select questionWithAnswer from QuestionWithAnser questionWithAnswe where questionWithAnswer.nextAppearance <= :today and questionWithAnswer.appUser = :appUser")
//	Set<QuestionWithAnswer> findAllByAppUserWithNextAppearanceBeforeToday(@Param("today") Date today,
//			@Param("appUser") AppUser appUser);
	public Set<QuestionWithAnswer> findByNextAppearanceLessThanEqualAndAppUser(Date today, AppUser appUser);

	public List<QuestionWithAnswer> findByAppUserAndQuestion(AppUser appUser, String question);
	
	public List<QuestionWithAnswer> findByAppUserAndIndexBox(AppUser appUser, IndexBox indexBox);
}
