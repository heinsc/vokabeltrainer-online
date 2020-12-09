package de.heins.vokabeltraineronline.web.controller;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MenuController {
	@Autowired
	private LearnFilterIndexBoxesController learnFilterIndexBoxesController;
	@Autowired
	private ManageConfigurationsController manageConfigurationsController;
	@Autowired
	private EditAppUserController editAppUserController;
	@Autowired
	private DeleteAppUserController deleteAppUserController;
	@Autowired
	private ManageQuestionsWithAnswersController manageQuestionsWithAnswersController;
	private static enum Constants {
		menuPage
	}
	public MenuController() {
		super();
	}

	public String showMenuPage(//
			Model model//
			, StandardSessionFacade session//
	)  {
		return Constants.menuPage.name();
	}
	@RequestMapping(value = "/controlActionMenu", method = RequestMethod.POST, params = {"manageConfigurations"})
	public String manageConfigurations(Model model, StandardSessionFacade session) {
		return manageConfigurationsController.showManageConfigurationsPage(model, session);
	}
	@RequestMapping(value = "/controlActionMenu", method = RequestMethod.POST, params = {"learn"})
	public String learn(Model model, StandardSessionFacade session) {
		return learnFilterIndexBoxesController.showLearnFilterIndexBoxesPage(session, model);

	}
	@RequestMapping(value = "/controlActionMenu", method = RequestMethod.POST, params = {"manageQuestionsWithAnswers"})
	public String manageQuestionsWithAnswers(Model model, StandardSessionFacade session) {
		return manageQuestionsWithAnswersController.showManageQuestionsWithAnswersPage(model, session);

	}
	@RequestMapping(value = "/controlActionMenu", method = RequestMethod.POST, params = {"editAppUser"})
	public String editAppUser(Model model, StandardSessionFacade session) {
		// direct link to ManangeAppUserController
		return editAppUserController.showEditAppUserPage(model, session);

	}
	@RequestMapping(value = "/controlActionMenu", method = RequestMethod.POST, params = {"deleteAppUser"})
	public String deleteAppUser(Model model, StandardSessionFacade session) {
		return deleteAppUserController.showDeleteAppUserPage(model, session);

	}


}