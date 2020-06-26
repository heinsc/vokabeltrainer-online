package de.heins.vokabeltraineronline.business.service;

import java.util.Calendar;
import java.util.List;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;

import de.heins.vokabeltraineronline.business.repository.UserRepository;
import de.heins.vokabeltraineronline.business.entity.Student;
import de.heins.vokabeltraineronline.business.entity.UserFactory;
import de.heins.vokabeltraineronline.web.controller.VokabeltrainerException;
import de.heins.vokabeltraineronline.web.entities.RegisterForm;

@Service
public class UserService {
	@Autowired
	private UserFactory userFactory;
	@Autowired
	private UserRepository userRepository;
	public void addUser(RegisterForm registerForm) throws VokabeltrainerException {
		try { 
			List<Student> findByEmail = userRepository.findByEmail(registerForm.getEmail());
			if (!findByEmail.isEmpty()) {
				throw new VokabeltrainerException();
			}
		} catch (InvalidDataAccessResourceUsageException e) {
			// this occurs only when there are no items in the database table.
			// nothing to do then.
		}

		Student user = userFactory//
			.setEMail(registerForm.getEmail())//
			.setPassword(registerForm.getPassword())//
			.setLastLogin(Calendar.getInstance().getTime())//
			.getNewObject();
		userRepository.save(user);
	
	}
}
