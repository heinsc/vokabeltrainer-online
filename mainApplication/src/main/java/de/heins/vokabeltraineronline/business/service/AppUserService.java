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
import de.heins.vokabeltraineronline.business.entity.BehaviourIfPoolWithWrongAnswersIsFull;
import de.heins.vokabeltraineronline.web.controller.AppUserAlreadyExistsException;
import de.heins.vokabeltraineronline.web.controller.WrongPasswordException;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.AppUserAttrRef;


@Service
public class AppUserService {
	@Autowired
	private AppUserFactory appUserFactory;
	@Autowired
	private AppUserRepository appUserRepository;
	public SessionAppUser addAppUser(AppUserAttrRef appUserAttrRef) throws AppUserAlreadyExistsException {
		checkAppUserAlreadyExists(appUserAttrRef);
		AppUser appUser = appUserFactory//
			.setEMail(appUserAttrRef.getEmail())//
			.setPassword(appUserAttrRef.getPassword())//
			.setBehaviourIfPoolWithWrongAnswersIsFull(//
					BehaviourIfPoolWithWrongAnswersIsFull.FILL_POOL_IMMEADLY//
			).setMaxNumberOfWrongAnswersPerSession(5)
			.setLastLogin(Calendar.getInstance().getTime())//
			.getNewObject();
		appUserRepository.save(appUser);
		SessionAppUser sessionAppUserForm = new SessionAppUser();
		sessionAppUserForm.setEmail(appUser.getEmail());
		sessionAppUserForm.setId(appUser.getId());
		return sessionAppUserForm;
	}
	private void checkAppUserAlreadyExists(AppUserAttrRef appUser) throws AppUserAlreadyExistsException {
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

	public SessionAppUser getSessionAppUserForLogin(AppUserAttrRef appUserForm) throws WrongPasswordException {

		List<AppUser>  findByEmailAndPassword = appUserRepository.findByEmailAndPassword(//
				appUserForm.getEmail()//
				, appUserForm.getPassword()//
		);
		if (!findByEmailAndPassword .isEmpty()) {
			AppUser appUser = findByEmailAndPassword.get(0);
			appUser.setLastLogin(Calendar.getInstance().getTime());
			appUserRepository.save(appUser);
			SessionAppUser sessionAppUserForm = new SessionAppUser();
			sessionAppUserForm.setEmail(appUser.getEmail());
			sessionAppUserForm.setId(appUser.getId());
			return sessionAppUserForm;
		} 
		throw new WrongPasswordException();
	}

	public void updateAppUserByEditAppUser(//
			Long id//
			, String originalEmail//
			, AppUserAttrRef appUserForm//
			, String newPassword//
	) throws AppUserAlreadyExistsException, WrongPasswordException {
		if (!originalEmail.equals(appUserForm.getEmail())) {
			checkAppUserAlreadyExists(appUserForm);
			if (Strings.isEmpty(newPassword)) {
				tryPasswordValidationForEditAppUserOrDeleteAppUser(originalEmail, appUserForm.getPassword());
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
	private void tryPasswordValidationForEditAppUserOrDeleteAppUser(String eMail, String password) throws WrongPasswordException {
	
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
		tryPasswordValidationForEditAppUserOrDeleteAppUser(email, password);
		appUserRepository.deleteById(id);
	}
}
