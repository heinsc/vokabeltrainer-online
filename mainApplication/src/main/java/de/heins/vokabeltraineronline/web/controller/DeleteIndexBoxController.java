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
		, deleteIndexBoxModAtt
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
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		SessionAppUser sessionAppUser = (SessionAppUser) session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		String sessionOldVersionOfIndexBoxName = (String) session.getAttribute(//
				ControllerConstants.sessionOldVersionOfIndexBoxName.name()//
		);
		String sessionOldVersionOfIndexBoxSubject = (String) session.getAttribute(//
				ControllerConstants.sessionOldVersionOfIndexBoxSubject.name()//
		);
		IndexBoxAttrRef indexBox = indexBoxService.findForAppUserAndNameAndSubject(//
				sessionAppUser//
				, sessionOldVersionOfIndexBoxName//
				, sessionOldVersionOfIndexBoxSubject//
		);
		
		DeleteIndexBoxModAtt deleteIndexBoxModAtt = new DeleteIndexBoxModAtt();
		deleteIndexBoxModAtt.setIndexBox(indexBox);
		model.addAttribute(//
				Constants.deleteIndexBoxModAtt.name()//
				, deleteIndexBoxModAtt//
		);
		return Constants.deleteIndexBoxPage.name();

	}
	@RequestMapping(value = "/controlActionDeleteIndexBox", method = RequestMethod.POST, params = {"cancel"})
	public String cancel(Model model, StandardSessionFacade session) {
		session.removeAttribute(ControllerConstants.sessionOldVersionOfIndexBoxName.name());
		session.removeAttribute(ControllerConstants.sessionOldVersionOfIndexBoxSubject.name());
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
				
				// TODO eigentliches Delete...
				session.removeAttribute(ControllerConstants.sessionOldVersionOfIndexBoxName.name());
				session.removeAttribute(ControllerConstants.sessionOldVersionOfIndexBoxSubject.name());
				return manageConfigurationsController.showManageConfigurationsPage(model, session);
			} catch (WrongPasswordException e) {
				deleteIndexBoxModAtt.setWrongPassword(true);
			}
		}
		return Constants.deleteIndexBoxModAtt.name();
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
