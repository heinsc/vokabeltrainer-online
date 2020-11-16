package de.heins.vokabeltraineronline.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltraineronline.business.entity.AppUser;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long>{

	public List<AppUser> findByEmail(String email);

	public List<AppUser> findByEmailAndPassword(String email, String password);

}
