package de.heins.vokabeltrainerdropmaindatabase.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerdropmaindatabase.entity.LearningStrategy;

@Repository
public interface LearningStrategyRepository extends CrudRepository<LearningStrategy, Long>{
}
