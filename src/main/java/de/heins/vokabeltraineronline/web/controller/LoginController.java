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

import java.util.Calendar;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.heins.vokabeltraineronline.web.entities.AuthentificationForm;

@Controller
public class LoginController {

	@ModelAttribute("today")
	public Calendar dateToday() {
		return Calendar.getInstance();
	}

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
	) {
		if (//
		Strings.isBlank(authentificationForm.getEmail())//
				|| Strings.isBlank(authentificationForm.getPassword())//
		) {
			authentificationForm.setMandatory(true);
		} else {
			if (// simulate success
			"success".equals(authentificationForm.getEmail())//
			) {
				authentificationForm.setLoginOK(true);
			} else {
				authentificationForm.setLoginError(true);
			}

		}
		return "login";

	}
}
