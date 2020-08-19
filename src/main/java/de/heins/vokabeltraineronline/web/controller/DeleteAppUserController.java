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
public class DeleteAppUserController {
	@Autowired
	private AppUserService appUserService;

	public DeleteAppUserController() {
		super();
	}

	@RequestMapping({ "/deleteAppUser" })
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
		return "deleteAppUser";

	}

	@RequestMapping(value = "/doDeleteAppUser", method = RequestMethod.POST)
	public String submitChanges(//
			@ModelAttribute(value = "register") RegisterForm registerForm//
			, HttpSession session
	) {
		SessionAppUserForm sessionAppUserForm = (SessionAppUserForm)session.getAttribute("sessionAppUser");
		if (checkFields(registerForm, sessionAppUserForm)) {
			AppUserForm appUserForm = registerForm.getAppUser();
			try {
				appUserService.deleteAppUser(sessionAppUserForm.getId(), sessionAppUserForm.getEmail(), appUserForm.getPassword());
				return "redirect:/login";
			} catch (WrongPasswordException e) {
				registerForm.setWrongPassword(true);
			}
		}
		return "deleteAppUser";
	}

	protected boolean checkFields(RegisterForm registerForm, SessionAppUserForm sessionAppUser) {
		if (//
				Strings.isBlank(registerForm.getAppUser().getPassword())//
		) {
			registerForm.setMandatoryViolated(true);
			return false;
		}
		return true;
	}
}
