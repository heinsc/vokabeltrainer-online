package de.heins.vokabeltrainerbackup.business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerbackup.business.entity.SuccessStep;

@Repository
public interface SuccessStepRepository extends CrudRepository<SuccessStep, Long>{

	@Query(value = "SELECT MAX(id) FROM SuccessStep")
	public List<Long> getMaxId();

}
