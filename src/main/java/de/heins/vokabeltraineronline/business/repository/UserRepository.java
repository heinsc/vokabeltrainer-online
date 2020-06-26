package de.heins.vokabeltraineronline.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltraineronline.business.entity.Student;

@Repository
public interface UserRepository extends CrudRepository<Student, Long>{

	public List<Student> findByEmail(String email);

}
