package de.heins.vokabeltraineronline.web.controller;


import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ManageIndexBoxController {

	public ManageIndexBoxController() {
		super();
	}

	@RequestMapping({ "/manageIndexBox" })
	public String manageUser(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		return "manageIndexBox";

	}

}
