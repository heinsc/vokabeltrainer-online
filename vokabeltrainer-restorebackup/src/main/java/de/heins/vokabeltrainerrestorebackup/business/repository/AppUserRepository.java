package de.heins.vokabeltrainerrestorebackup.business.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerrestorebackup.business.entity.AppUser;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long>{

	List<AppUser> findByEmail(String email);
	

}
