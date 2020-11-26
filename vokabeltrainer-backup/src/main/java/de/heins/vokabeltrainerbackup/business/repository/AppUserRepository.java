package de.heins.vokabeltrainerbackup.business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerbackup.business.entity.AppUser;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long>{

	@Query(value = "SELECT MAX(id) FROM AppUser")
	public List<Long> getMaxId();

}
