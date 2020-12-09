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
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.CreateAppUserModAtt;

@Controller
public class CreateAppUserController {
	private static enum Constants {
		createAppUserPage, createAppUserModAtt
	}
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private MenuController menuController;
	@Autowired
	private LoginController loginController;

	public CreateAppUserController() {
		super();
	}

	public String showCreateAppUserPage(Model model) {
		model.addAttribute(Constants.createAppUserModAtt.name(), new CreateAppUserModAtt());
		return Constants.createAppUserPage.name();

	}
	@RequestMapping(value = "/controlActionCreateAppUser" , method = RequestMethod.POST, params= {"cancel"})
	public String cancel(Model model, StandardSessionFacade session) {
		return loginController.showLoginPage(model, session);
	}

	@RequestMapping(value = "/controlActionCreateAppUser", method = RequestMethod.POST, params= {"submit"})
	public String addAccount(//
			@ModelAttribute(name = "createAppUserModAtt")
			CreateAppUserModAtt createAppUser
			, StandardSessionFacade session
			, Model model
	) {
		if (checkFields(createAppUser)) {
			try {
				SessionAppUser sessionAppUser = appUserService.addAppUser(createAppUser.getAppUser());
				session.setAttribute(//
						ControllerConstants.sessionAppUser.name()//
						, sessionAppUser//
				);
				return menuController.showMenuPage(model, session);
			} catch (AppUserAlreadyExistsException e) {
				createAppUser.setAppUserAlreadyExists(true);
			}
		}
		return Constants.createAppUserPage.name();
	}

	private boolean checkPasswords(CreateAppUserModAtt registerForm) {
		if (// 
				!registerForm.getAppUser().getPassword().equals(registerForm.getPasswordRepeated())
		) {
			registerForm.setPasswordsNotEqual(true);
			return false;
		}
		return true;
	}
	private boolean checkFields(CreateAppUserModAtt registerForm) {
		if (//
				Strings.isBlank(registerForm.getAppUser().getEmail())//
				|| Strings.isBlank(registerForm.getAppUser().getPassword())//
				|| Strings.isBlank(registerForm.getPasswordRepeated())
		) {
			registerForm.setMandatoryViolated(true);
			return false;
		}
		return checkPasswords(registerForm);
	}

}
