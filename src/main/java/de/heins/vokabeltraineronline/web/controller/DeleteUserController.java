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

import org.apache.catalina.session.StandardSessionFacade;
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
import de.heins.vokabeltraineronline.web.entities.UserForm;

@Controller
public class DeleteUserController {
	@Autowired
	private UserService userService;

	public DeleteUserController() {
		super();
	}

	@RequestMapping({ "/deleteUser" })
	public String manageUser(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		SessionUserForm sessioUser = (SessionUserForm) session.getAttribute("sessionUser");
		UserForm userForEdit = new UserForm();
		userForEdit.setEmail(sessioUser.getEmail());
		RegisterForm registerForm = new RegisterForm();
		registerForm.setUser(userForEdit);
		model.addAttribute("register", registerForm);
		return "deleteUser";

	}

	@RequestMapping(value = "/doDeleteUser", method = RequestMethod.POST)
	public String submitChanges(//
			@ModelAttribute(value = "register") RegisterForm registerForm//
			, HttpSession session
	) {
		SessionUserForm sessionUserForm = (SessionUserForm)session.getAttribute("sessionUser");
		if (checkFields(registerForm, sessionUserForm)) {
			UserForm userForm = registerForm.getUser();
			try {
				userService.deleteUser(sessionUserForm.getId(), sessionUserForm.getEmail(), userForm.getPassword());
				return "redirect:/login";
			} catch (WrongPasswordException e) {
				registerForm.setWrongPassword(true);
			}
		}
		return "deleteUser";
	}

	protected boolean checkFields(RegisterForm registerForm, SessionUserForm sessionUser) {
		if (//
				Strings.isBlank(registerForm.getUser().getPassword())//
		) {
			registerForm.setMandatoryViolated(true);
			return false;
		}
		return true;
	}
}
