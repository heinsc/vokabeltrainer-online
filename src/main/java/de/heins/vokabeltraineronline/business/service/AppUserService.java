package de.heins.vokabeltraineronline.business.service;

import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;

import de.heins.vokabeltraineronline.business.repository.AppUserRepository;
import de.heins.vokabeltraineronline.business.entity.AppUser;
import de.heins.vokabeltraineronline.business.entity.AppUserFactory;
import de.heins.vokabeltraineronline.web.controller.AppUserAlreadyExistsException;
import de.heins.vokabeltraineronline.web.controller.WrongPasswordException;
import de.heins.vokabeltraineronline.web.entities.SessionAppUserForm;
import de.heins.vokabeltraineronline.web.entities.AppUserForm;


@Service
public class AppUserService {
	@Autowired
	private AppUserFactory appUserFactory;
	@Autowired
	private AppUserRepository appUserRepository;
	public SessionAppUserForm addAppUser(AppUserForm appUserForm) throws AppUserAlreadyExistsException {
		checkAppUserAlreadyExists(appUserForm);
		AppUser appUser = appUserFactory//
			.setEMail(appUserForm.getEmail())//
			.setPassword(appUserForm.getPassword())//
			.setLastLogin(Calendar.getInstance().getTime())//
			.getNewObject();
		appUserRepository.save(appUser);
		SessionAppUserForm sessionAppUserForm = new SessionAppUserForm();
		sessionAppUserForm.setEmail(appUser.getEmail());
		sessionAppUserForm.setId(appUser.getId());
		return sessionAppUserForm;
	}
	private void checkAppUserAlreadyExists(AppUserForm appUser) throws AppUserAlreadyExistsException {
		try { 
			List<AppUser> findByEmail = appUserRepository.findByEmail(appUser.getEmail());
			if (!findByEmail.isEmpty()) {
				throw new AppUserAlreadyExistsException();
			}
		} catch (InvalidDataAccessResourceUsageException e) {
			// this occurs only when there are no items in the database table.
			// nothing to do then.
		}
	}

	public SessionAppUserForm getSessionAppUserForLogin(AppUserForm appUserForm) throws WrongPasswordException {

		List<AppUser>  findByEmailAndPassword = appUserRepository.findByEmailAndPassword(//
				appUserForm.getEmail()//
				, appUserForm.getPassword()//
		);
		if (!findByEmailAndPassword .isEmpty()) {
			AppUser appUser = findByEmailAndPassword.get(0);
			appUser.setLastLogin(Calendar.getInstance().getTime());
			appUserRepository.save(appUser);
			SessionAppUserForm sessionAppUserForm = new SessionAppUserForm();
			sessionAppUserForm.setEmail(appUser.getEmail());
			sessionAppUserForm.setId(appUser.getId());
			return sessionAppUserForm;
		} 
		throw new WrongPasswordException();
	}

	public void updateAppUserByManageAppUser(Long id, String originalEmail, AppUserForm appUserForm, String newPassword) throws AppUserAlreadyExistsException, WrongPasswordException {
		if (!originalEmail.equals(appUserForm.getEmail())) {
			checkAppUserAlreadyExists(appUserForm);
			if (Strings.isEmpty(newPassword)) {
				tryPasswordValidationForManageAppUserOrDeleteAppUser(originalEmail, appUserForm.getPassword());
			}

		}
		//prepare if only Password has to be changed
		if (Strings.isEmpty(appUserForm.getEmail())) {
			appUserForm.setEmail(originalEmail);
		}
		AppUser appUser = appUserFactory//
			.setEMail(appUserForm.getEmail())//
			.setPassword(appUserForm.getPassword())//
			.setLastLogin(Calendar.getInstance().getTime())//
			.setId(id)
			.getNewObject();
		appUserRepository.save(appUser);
		
	}
	private void tryPasswordValidationForManageAppUserOrDeleteAppUser(String eMail, String password) throws WrongPasswordException {
	
		List<AppUser>  findByEmailAndPassword = appUserRepository.findByEmailAndPassword(//
				eMail//
				, password//
		);
		if (findByEmailAndPassword .isEmpty()) {
			throw new WrongPasswordException();
		} 
		return;
	}
	public void deleteAppUser(Long id, String email, String password)  throws WrongPasswordException {
		tryPasswordValidationForManageAppUserOrDeleteAppUser(email, password);
		appUserRepository.deleteById(id);
	}
}
