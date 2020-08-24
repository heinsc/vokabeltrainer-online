package de.heins.vokabeltraineronline.web.controller;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MenuController {
	private static enum Constants {
		menuPage
	}
	public MenuController() {
		super();
	}

	@RequestMapping("/controlMenu")
	public String showMenuPage(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		return Constants.menuPage.name();

	}
	@RequestMapping(value = "/controlActionMenu", method = RequestMethod.POST, params = {"manageConfigurations"})
	public String manageConfigurations() throws Exception {
		// direct link to ManangeConfigurationsController
		return "redirect:" + ControllerConstants.controlManageConfigurations.name();

	}
	@RequestMapping(value = "/controlActionMenu", method = RequestMethod.POST, params = {"editAppUser"})
	public String editAppUser() throws Exception {
		// direct link to ManangeAppUserController
		return "redirect:" + ControllerConstants.controlEditAppUser.name();

	}
	@RequestMapping(value = "/controlActionMenu", method = RequestMethod.POST, params = {"deleteAppUser"})
	public String deleteAppUser() throws Exception {
		//direct Link to DeleteAppUserController
		return "redirect:" + ControllerConstants.controlDeleteAppUser.name();

	}
	@RequestMapping(value = "/controlActionMenu", method = RequestMethod.POST, params = {"logout"})
	public String logout() throws Exception {
		//direct Link to DeleteAppUserController
		return "redirect:" + ControllerConstants.controlLogin.name();
	}


}