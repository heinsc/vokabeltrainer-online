package de.heins.vokabeltraineronline.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class InsertionsController {
	@RequestMapping(value="/controlActionInsertions", method=RequestMethod.POST, params= {"logout"})
	public String logout() {
		//direct go to login 
		return "redirect:" + ControllerConstants.controlPageLogin.name();
	}


}
