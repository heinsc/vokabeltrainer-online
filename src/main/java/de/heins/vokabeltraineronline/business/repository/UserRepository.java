package de.heins.vokabeltraineronline.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltraineronline.business.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

	public List<User> findByEmail(String email);

	public List<User> findByEmailAndPassword(String email, String password);

}
