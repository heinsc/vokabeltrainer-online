package de.heins.vokabeltraineronline.business.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;

import de.heins.vokabeltraineronline.business.repository.UserRepository;
import de.heins.vokabeltraineronline.business.entity.Student;
import de.heins.vokabeltraineronline.business.entity.UserFactory;
import de.heins.vokabeltraineronline.web.controller.VokabeltrainerException;
import de.heins.vokabeltraineronline.web.entities.AuthentificationForm;
import de.heins.vokabeltraineronline.web.entities.RegisterForm;
import org.apache.logging.log4j.util.Strings;

@Service
public class UserService {
	@Autowired
	private UserFactory userFactory;
	@Autowired
	private UserRepository userRepository;
	public void addUser(RegisterForm registerForm) throws VokabeltrainerException {
		try { 
			List<Student> findByEmail = userRepository.findByEmail(registerForm.getUser().getEmail());
			if (!findByEmail.isEmpty()) {
				throw new VokabeltrainerException();
			}
		} catch (InvalidDataAccessResourceUsageException e) {
			// this occurs only when there are no items in the database table.
			// nothing to do then.
		}

		Student user = userFactory//
			.setEMail(registerForm.getUser().getEmail())//
			.setPassword(registerForm.getUser().getPassword())//
			.setLastLogin(Calendar.getInstance().getTime())//
			.getNewObject();
		userRepository.save(user);
	
	}
	public void checkLogin(AuthentificationForm authentificationForm) {
		if (//
				Strings.isBlank(authentificationForm.getUser().getEmail())//
				|| Strings.isBlank(authentificationForm.getUser().getPassword())//
		) {
			authentificationForm.setMandatoryViolated(true);
		} else {
			List<Student> findByEmailAndPassword = userRepository.findByEmailAndPassword(//
					authentificationForm.getUser().getEmail()//
					, authentificationForm.getUser().getPassword()//
				);
			if (!findByEmailAndPassword.isEmpty()) {
				authentificationForm.setLoginOK(true);
			} else {
				authentificationForm.setLoginError(true);
			}
		}
	}
}
