package de.heins.vokabeltrainerbackup.business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerbackup.business.entity.LearningStrategy;

@Repository
public interface LearningStrategyRepository extends CrudRepository<LearningStrategy, Long>{

	@Query(value = "SELECT MAX(id) FROM LearningStrategy")
	public List<Long> getMaxId();

}
