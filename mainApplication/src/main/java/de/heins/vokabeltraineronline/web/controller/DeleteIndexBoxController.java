package de.heins.vokabeltraineronline.web.controller;

import org.apache.catalina.session.StandardSessionFacade;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.heins.vokabeltraineronline.business.service.AppUserService;
import de.heins.vokabeltraineronline.business.service.IndexBoxService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.AppUserAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.DeleteIndexBoxModAtt;

@Controller
public class DeleteIndexBoxController {
	private static enum Constants {
		deleteIndexBoxPage//
		, deleteIndexBoxModAtt//
		, sessionDeleteIndexBox//
	}
	@Autowired
	private ManageConfigurationsController manageConfigurationsController;
	@Autowired
	private AppUserService appUserService;
	
	@Autowired
	private IndexBoxService indexBoxService;

	public DeleteIndexBoxController() {
		super();
	}

	public String showDeleteIndexBoxPage(//
			String oldVersionOfIndexBoxName//
			, String oldVersionOfIndexBoxSubject//
			, Model model//
			, StandardSessionFacade session//
	) {
		SessionAppUser sessionAppUser = (SessionAppUser) session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		IndexBoxAttrRef indexBox = indexBoxService.findForAppUserAndNameAndSubject(//
				sessionAppUser//
				, oldVersionOfIndexBoxName//
				, oldVersionOfIndexBoxSubject//
		);
		
		DeleteIndexBoxModAtt deleteIndexBoxModAtt = new DeleteIndexBoxModAtt();
		model.addAttribute(Constants.deleteIndexBoxModAtt.name(), deleteIndexBoxModAtt);
		session.setAttribute(//
				Constants.sessionDeleteIndexBox.name()//
				, indexBox//
		);
		return Constants.deleteIndexBoxPage.name();

	}
	@RequestMapping(value = "/controlActionDeleteIndexBox", method = RequestMethod.POST, params = {"cancel"})
	public String cancel(Model model, StandardSessionFacade session) {
		return backToManageConfigurationPage(model, session);
	}

	private String backToManageConfigurationPage(Model model, StandardSessionFacade session) {
		session.removeAttribute(Constants.sessionDeleteIndexBox.name());
		return manageConfigurationsController.showManageConfigurationsPage(model, session);
	}
	@RequestMapping(value = "/controlActionDeleteIndexBox", method = RequestMethod.POST, params = {"delete"})
	public String delete(//
			@ModelAttribute(name = "deleteIndexBoxModAtt")
			DeleteIndexBoxModAtt deleteIndexBoxModAtt//
			, Model model//
			, StandardSessionFacade session
	) {
		SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		AppUserAttrRef appUserAttrRef = new AppUserAttrRef();
		appUserAttrRef.setEmail(sessionAppUser.getEmail());
		appUserAttrRef.setPassword(deleteIndexBoxModAtt.getPassword());
		if (checkFields(deleteIndexBoxModAtt)) {
			try {
				appUserService.getSessionAppUserForLogin(appUserAttrRef);
				IndexBoxAttrRef indexBoxAttrRef = (IndexBoxAttrRef) session.getAttribute(Constants.sessionDeleteIndexBox.name());
				//TODO eigentliches delete
				return backToManageConfigurationPage(model, session);
			} catch (WrongPasswordException e) {
				deleteIndexBoxModAtt.setWrongPassword(true);
			}
		}
		return Constants.deleteIndexBoxPage.name();
	}

	private boolean checkFields(DeleteIndexBoxModAtt deleteIndexBoxModAtt) {
		if (//
				Strings.isBlank(deleteIndexBoxModAtt.getPassword())//
		) {
			deleteIndexBoxModAtt.setMandatoryViolated(true);
			
			return false;
		}
		return true;
	}
}
