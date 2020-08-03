package de.heins.vokabeltraineronline.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltraineronline.business.entity.LearningStrategy;
import de.heins.vokabeltraineronline.business.entity.SuccessStep;
import de.heins.vokabeltraineronline.business.entity.User;

@Repository
public interface SuccessStepRepository extends CrudRepository<SuccessStep, Long>{
	public List<SuccessStep> findByUser(User user);
	public List<SuccessStep> findByUserAndLearningStrategy(User user, LearningStrategy learningStrategy);

}
