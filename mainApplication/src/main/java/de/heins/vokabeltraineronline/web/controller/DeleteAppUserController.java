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

import de.heins.vokabeltraineronline.business.service.AppUserService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.AppUserAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.DeleteAppUserModAtt;

@Controller
public class DeleteAppUserController {
	private static enum Constants {
		deleteAppUserPage, deleteAppUserModAtt
	}
	@Autowired
	private AppUserService appUserService;

	public DeleteAppUserController() {
		super();
	}

	@RequestMapping({ "/controlPageDeleteAppUser" })
	public String showDeleteAppUserPage(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		SessionAppUser sessioAppUser = (SessionAppUser) session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		AppUserAttrRef appUserForEdit = new AppUserAttrRef();
		appUserForEdit.setEmail(sessioAppUser.getEmail());
		DeleteAppUserModAtt deleteAppUserModAtt = new DeleteAppUserModAtt();
		deleteAppUserModAtt.setAppUser(appUserForEdit);
		model.addAttribute(//
				Constants.deleteAppUserModAtt.name()//
				, deleteAppUserModAtt//
		);
		return Constants.deleteAppUserPage.name();

	}
	@RequestMapping(value = "/controlActionDeleteAppUser", method = RequestMethod.POST, params = {"cancel"})
	public String cancel() {
		return "redirect:" + ControllerConstants.controlPageMenu.name();
	}
	@RequestMapping(value = "/controlActionDeleteAppUser", method = RequestMethod.POST, params = {"delete"})
	public String delete(//
			@ModelAttribute(name = "deleteAppUserModAtt")
			DeleteAppUserModAtt deleteAppUser//
			, HttpSession session
	) {
		SessionAppUser sessionAppUserForm = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		if (checkFields(deleteAppUser)) {
			AppUserAttrRef appUserForm = deleteAppUser.getAppUser();
			try {
				appUserService.deleteAppUser(sessionAppUserForm.getId(), sessionAppUserForm.getEmail(), appUserForm.getPassword());
				return "redirect:" + ControllerConstants.controlPageLogin.name();
			} catch (WrongPasswordException e) {
				deleteAppUser.setWrongPassword(true);
			}
		}
		return Constants.deleteAppUserPage.name();
	}

	private boolean checkFields(DeleteAppUserModAtt deleteAppUserModAtt) {
		if (//
				Strings.isBlank(deleteAppUserModAtt.getAppUser().getPassword())//
		) {
			deleteAppUserModAtt.setMandatoryViolated(true);
			return false;
		}
		return true;
	}
}
