package de.heins.vokabeltrainerdropmaindatabase.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerdropmaindatabase.entity.SuccessStep;

@Repository
public interface SuccessStepRepository extends CrudRepository<SuccessStep, Long>{

}
