package de.heins.vokabeltraineronline.web.controller;

import java.util.Enumeration;

import org.apache.catalina.session.StandardSessionFacade;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.heins.vokabeltraineronline.business.service.AppUserService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.LoginModAtt;

@Controller
public class LoginController {
	private static enum Constants {
		loginPage, loginModAtt
	}
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private MenuController menuController;
	@Autowired
	private CreateAppUserController createAppUserController;
	public LoginController() {
		super();
	}

	@RequestMapping({ "/", "/login"})
	public String showLoginPage(Model model, StandardSessionFacade session) {
		Enumeration<String> attributeNames = session.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String currentAttributeName = (String) attributeNames.nextElement();
			session.removeAttribute(currentAttributeName);
		}
		LoginModAtt loginModelAttribute = new LoginModAtt();
		model.addAttribute(Constants.loginModAtt.name(), loginModelAttribute);
		return Constants.loginPage.name();

	}

	@RequestMapping(value = "/controlActionLogin", params = {"login"}, method = RequestMethod.POST)
	public String checkLogin(//
			@ModelAttribute(name = "loginModAtt")
			LoginModAtt loginModelAttribute
			, StandardSessionFacade session//
			, Model model
	) {
		if (//
				Strings.isBlank(loginModelAttribute.getAppUser().getEmail())//
				|| Strings.isBlank(loginModelAttribute.getAppUser().getPassword())//
		) {
			loginModelAttribute.setMandatoryViolated(true);
			return Constants.loginPage.name();
		}
		try {
			SessionAppUser sessionAppUserForLogin = appUserService.getSessionAppUserForLogin(loginModelAttribute.getAppUser());
			session.setAttribute(//
					ControllerConstants.sessionAppUser.name()//
					, sessionAppUserForLogin//
			);
			return menuController.showMenuPage(model, session);
		} catch (WrongPasswordException e) {
			// wrong appUser credentials
			loginModelAttribute.setLoginError(true);
			return Constants.loginPage.name();
		}
	}
	@RequestMapping(value = "/controlActionLogin", params = {"createAppUser"}, method = RequestMethod.POST)
	public String createAppUser(Model model) {
		return createAppUserController.showCreateAppUserPage(model);
	}

}
