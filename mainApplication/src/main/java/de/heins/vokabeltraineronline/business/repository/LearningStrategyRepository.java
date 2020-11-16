package de.heins.vokabeltraineronline.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltraineronline.business.entity.LearningStrategy;
import de.heins.vokabeltraineronline.business.entity.AppUser;

@Repository
public interface LearningStrategyRepository extends CrudRepository<LearningStrategy, Long>{
	public List<LearningStrategy> findByAppUser(AppUser appUser);

	public List<LearningStrategy> findByAppUserAndName(AppUser appUser, String oldVersionOfLearningStrategyName);

}
