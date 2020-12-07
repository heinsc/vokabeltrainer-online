package de.heins.vokabeltraineronline.web.controller;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class InsertionsController {
	@Autowired
	private LoginController loginController;
	@RequestMapping(value="/controlActionInsertions", method=RequestMethod.POST, params= {"logout"})
	public String logout(StandardSessionFacade session, Model model) {
		//direct go to login 
		return loginController.showLoginPage(model, session);
	}


}
