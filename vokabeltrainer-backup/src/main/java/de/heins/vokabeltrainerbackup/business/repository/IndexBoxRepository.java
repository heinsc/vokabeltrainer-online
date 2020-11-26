package de.heins.vokabeltrainerbackup.business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerbackup.business.entity.IndexBox;


@Repository
public interface IndexBoxRepository extends CrudRepository<IndexBox, Long>{

	@Query(value = "SELECT MAX(id) FROM IndexBox")
	public List<Long> getMaxId();

}
