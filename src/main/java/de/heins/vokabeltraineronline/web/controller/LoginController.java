/*
 * =============================================================================
 * 
 *   Copyright (c) 2011-2016, The THYMELEAF team (http://www.thymeleaf.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package de.heins.vokabeltraineronline.web.controller;

import org.apache.catalina.session.StandardSessionFacade;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.heins.vokabeltraineronline.business.service.UserService;
import de.heins.vokabeltraineronline.web.entities.AuthentificationForm;
import de.heins.vokabeltraineronline.web.entities.SessionUserForm;

@Controller
public class LoginController {
	@Autowired
	UserService userService;

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
				Strings.isBlank(authentificationForm.getUser().getEmail())//
				|| Strings.isBlank(authentificationForm.getUser().getPassword())//
		) {
			authentificationForm.setMandatoryViolated(true);
			return "login";
		}
		try {
			SessionUserForm sessionUserForLogin = userService.getSessionUserForLogin(authentificationForm.getUser());
			session.setAttribute("sessionUser", sessionUserForLogin);
			return "menu";
		} catch (WrongPasswordException e) {
			// wrong user credentials
			authentificationForm.setLoginError(true);
			return "login";
		}
	}
}
