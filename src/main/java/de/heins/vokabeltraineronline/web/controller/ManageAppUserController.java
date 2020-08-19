package de.heins.vokabeltraineronline.web.controller;

import javax.servlet.http.HttpSession;

import org.apache.catalina.session.StandardSessionFacade;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.heins.vokabeltraineronline.business.service.AppUserService;
import de.heins.vokabeltraineronline.web.entities.RegisterForm;
import de.heins.vokabeltraineronline.web.entities.SessionAppUserForm;
import de.heins.vokabeltraineronline.web.entities.AppUserForm;

@Controller
public class ManageAppUserController {
	@Autowired
	private AppUserService appUserService;

	public ManageAppUserController() {
		super();
	}

	@RequestMapping({ "/manageAppUser" })
	public String manageAppUser(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		SessionAppUserForm sessioAppUser = (SessionAppUserForm) session.getAttribute("sessionAppUser");
		AppUserForm appUserForEdit = new AppUserForm();
		appUserForEdit.setEmail(sessioAppUser.getEmail());
		RegisterForm registerForm = new RegisterForm();
		registerForm.setAppUser(appUserForEdit);
		model.addAttribute("register", registerForm);
		return "manageAppUser";

	}

	@RequestMapping(value = "/changeAppUser", method = RequestMethod.POST)
	public String submitChanges(//
			@ModelAttribute(value = "register") RegisterForm registerForm//
			, HttpSession session
	) {
		SessionAppUserForm sessionAppUserForm = (SessionAppUserForm)session.getAttribute("sessionAppUser");
		if (checkFields(registerForm, sessionAppUserForm)) {
			AppUserForm appUserForm = registerForm.getAppUser();
			// check if email already exists for another AppUser
			try {
				appUserService.updateAppUserByManageAppUser(sessionAppUserForm.getId(), sessionAppUserForm.getEmail(), appUserForm, registerForm.getPasswordRepeated());
				sessionAppUserForm.setEmail(!Strings.isEmpty(appUserForm.getEmail()) ? appUserForm.getEmail() : sessionAppUserForm.getEmail());
				return "menu";
			} catch (AppUserAlreadyExistsException e) {//
				registerForm.setAppUserAlreadyExists(true);
			} catch (WrongPasswordException e) {
				registerForm.setWrongPassword(true);
			}
		}
		return "manageAppUser";
	}

	private boolean checkPasswords(RegisterForm registerForm) {
		if (!Strings.isEmpty(registerForm.getPasswordRepeated())) {
			if (// 
					!registerForm.getAppUser().getPassword().equals(registerForm.getPasswordRepeated())
			) {
				registerForm.setPasswordsNotEqual(true);
				return false;
			}
		}
		return true;
	}
	protected boolean checkFields(RegisterForm registerForm, SessionAppUserForm sessionAppUser) {
		if (//
				Strings.isBlank(registerForm.getAppUser().getPassword())//
				|| (//
						Strings.isBlank(registerForm.getPasswordRepeated())//
						&& (//
								Strings.isEmpty(registerForm.getAppUser().getEmail())//
								|| registerForm.getAppUser().getEmail().equals(sessionAppUser.getEmail())
						)
				)
		) {
			registerForm.setMandatoryViolated(true);
			return false;
		}
		return checkPasswords(registerForm);
	}
}
