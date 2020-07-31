package de.heins.vokabeltraineronline.business.service;

import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;

import de.heins.vokabeltraineronline.business.repository.UserRepository;
import de.heins.vokabeltraineronline.business.entity.User;
import de.heins.vokabeltraineronline.business.entity.UserFactory;
import de.heins.vokabeltraineronline.web.controller.UserAlreadyExistsException;
import de.heins.vokabeltraineronline.web.controller.WrongPasswordException;
import de.heins.vokabeltraineronline.web.entities.SessionUserForm;
import de.heins.vokabeltraineronline.web.entities.UserForm;


@Service
public class UserService {
	@Autowired
	private UserFactory userFactory;
	@Autowired
	private UserRepository userRepository;
	public SessionUserForm addUser(UserForm userForm) throws UserAlreadyExistsException {
		checkUserAlreadyExists(userForm);
		User user = userFactory//
			.setEMail(userForm.getEmail())//
			.setPassword(userForm.getPassword())//
			.setLastLogin(Calendar.getInstance().getTime())//
			.getNewObject();
		userRepository.save(user);
		SessionUserForm sessionUserForm = new SessionUserForm();
		sessionUserForm.setEmail(user.getEmail());
		sessionUserForm.setId(user.getId());
		return sessionUserForm;
	}
	private void checkUserAlreadyExists(UserForm user) throws UserAlreadyExistsException {
		try { 
			List<User> findByEmail = userRepository.findByEmail(user.getEmail());
			if (!findByEmail.isEmpty()) {
				throw new UserAlreadyExistsException();
			}
		} catch (InvalidDataAccessResourceUsageException e) {
			// this occurs only when there are no items in the database table.
			// nothing to do then.
		}
	}

	public SessionUserForm getSessionUserForLogin(UserForm userForm) throws WrongPasswordException {

		List<User>  findByEmailAndPassword = userRepository.findByEmailAndPassword(//
				userForm.getEmail()//
				, userForm.getPassword()//
		);
		if (!findByEmailAndPassword .isEmpty()) {
			User user = findByEmailAndPassword.get(0);
			user.setLastLogin(Calendar.getInstance().getTime());
			userRepository.save(user);
			SessionUserForm sessionUserForm = new SessionUserForm();
			sessionUserForm.setEmail(user.getEmail());
			sessionUserForm.setId(user.getId());
			return sessionUserForm;
		} 
		throw new WrongPasswordException();
	}

	public void updateUserByManageUser(Long id, String originalEmail, UserForm userForm, String newPassword) throws UserAlreadyExistsException, WrongPasswordException {
		if (!originalEmail.equals(userForm.getEmail())) {
			checkUserAlreadyExists(userForm);
			if (Strings.isEmpty(newPassword)) {
				tryPasswordValidationForManageUserOrDeleteUser(originalEmail, userForm.getPassword());
			}

		}
		//prepare if only Password has to be changed
		if (Strings.isEmpty(userForm.getEmail())) {
			userForm.setEmail(originalEmail);
		}
		User user = userFactory//
			.setEMail(userForm.getEmail())//
			.setPassword(userForm.getPassword())//
			.setLastLogin(Calendar.getInstance().getTime())//
			.setId(id)
			.getNewObject();
		userRepository.save(user);
		
	}
	private void tryPasswordValidationForManageUserOrDeleteUser(String eMail, String password) throws WrongPasswordException {
	
		List<User>  findByEmailAndPassword = userRepository.findByEmailAndPassword(//
				eMail//
				, password//
		);
		if (findByEmailAndPassword .isEmpty()) {
			throw new WrongPasswordException();
		} 
		return;
	}
	public void deleteUser(Long id, String email, String password)  throws WrongPasswordException {
		tryPasswordValidationForManageUserOrDeleteUser(email, password);
		userRepository.deleteById(id);
	}
}
