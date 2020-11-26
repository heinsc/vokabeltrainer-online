package de.heins.vokabeltrainerdropmaindatabase.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerdropmaindatabase.entity.AppUser;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long>{
	

}
