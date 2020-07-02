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

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.heins.vokabeltraineronline.business.service.UserService;
import de.heins.vokabeltraineronline.web.entities.RegisterForm;
import de.heins.vokabeltraineronline.web.entities.SessionUserForm;

@Controller
public class RegisterUserController {
	@Autowired
	private UserService userService;

	public RegisterUserController() {
		super();
	}

	@RequestMapping({ "/register" })
	public String showStartPage(Model model) throws Exception {
		model.addAttribute("register", new RegisterForm());
		return "register";

	}

	@RequestMapping(value = "/addAccount", method = RequestMethod.POST)
	public String addAccount(//
			@ModelAttribute(value = "register") RegisterForm registerForm//
			, HttpSession session
	) {
		if (checkFields(registerForm)) {
			try {
				SessionUserForm sessionUserForm = userService.addUser(registerForm.getUser());
				session.setAttribute("sessionUser", sessionUserForm);
				return "menu";
			} catch (UserAlreadyExistsException e) {
				registerForm.setUserAlreadyExists(true);
			}
		}
		return "register";
	}

	private boolean checkPasswords(RegisterForm registerForm) {
		if (// 
				!registerForm.getUser().getPassword().equals(registerForm.getPasswordRepeated())
		) {
			registerForm.setPasswordsNotEqual(true);
			return false;
		}
		return true;
	}
	private boolean checkFields(RegisterForm registerForm) {
		if (//
				Strings.isBlank(registerForm.getUser().getEmail())//
				|| Strings.isBlank(registerForm.getUser().getPassword())//
				|| Strings.isBlank(registerForm.getPasswordRepeated())
		) {
			registerForm.setMandatoryViolated(true);
			return false;
		}
		return checkPasswords(registerForm);
	}

}
