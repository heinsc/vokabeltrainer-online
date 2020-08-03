package de.heins.vokabeltraineronline.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltraineronline.business.entity.LearningStrategy;
import de.heins.vokabeltraineronline.business.entity.User;

@Repository
public interface LearningStrategyRepository extends CrudRepository<LearningStrategy, Long>{
	public List<LearningStrategy> findByUser(User user);

}
