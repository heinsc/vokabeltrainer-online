package de.heins.vokabeltraineronline.web.controller;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MenuController {
	public MenuController() {
		super();
	}

	@RequestMapping("/menu")
	public String showMenu(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		return "menu";

	}
}