package de.heins.vokabeltraineronline.web.controller;

import org.apache.catalina.session.StandardSessionFacade;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.heins.vokabeltraineronline.business.service.AppUserService;
import de.heins.vokabeltraineronline.web.entities.AuthentificationForm;
import de.heins.vokabeltraineronline.web.entities.SessionAppUserForm;

@Controller
public class LoginController {
	@Autowired
	AppUserService appUserService;

	public LoginController() {
		super();
	}

	@RequestMapping({ "/", "/login" })
	public String showStartPage(Model model) throws Exception {
		model.addAttribute("authentification", new AuthentificationForm());
		return "login";

	}

	@RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
	public String checkLogin(//
			@ModelAttribute(value = "authentification") AuthentificationForm authentificationForm//
			, StandardSessionFacade session
	) {
		if (//
				Strings.isBlank(authentificationForm.getAppUser().getEmail())//
				|| Strings.isBlank(authentificationForm.getAppUser().getPassword())//
		) {
			authentificationForm.setMandatoryViolated(true);
			return "login";
		}
		try {
			SessionAppUserForm sessionAppUserForLogin = appUserService.getSessionAppUserForLogin(authentificationForm.getAppUser());
			session.setAttribute("sessionAppUser", sessionAppUserForLogin);
			return "menu";
		} catch (WrongPasswordException e) {
			// wrong appUser credentials
			authentificationForm.setLoginError(true);
			return "login";
		}
	}
}
