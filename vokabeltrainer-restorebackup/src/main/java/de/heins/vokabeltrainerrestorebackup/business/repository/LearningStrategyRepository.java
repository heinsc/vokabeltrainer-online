package de.heins.vokabeltrainerrestorebackup.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerrestorebackup.business.entity.AppUser;
import de.heins.vokabeltrainerrestorebackup.business.entity.LearningStrategy;

@Repository
public interface LearningStrategyRepository extends CrudRepository<LearningStrategy, Long>{
	public List<LearningStrategy> findByAppUserAndName(AppUser appUser, String oldVersionOfLearningStrategyName);
}
