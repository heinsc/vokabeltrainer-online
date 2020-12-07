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
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.AppUserAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.EditAppUserModAtt;

@Controller
public class EditAppUserController {
	private static enum Constants {
		editAppUserPage
		, editAppUserModAtt
	}
	@Autowired
	private MenuController menuController;
	@Autowired
	private AppUserService appUserService;

	public EditAppUserController() {
		super();
	}

	public String showEditAppUserPage(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		SessionAppUser sessioAppUser = (SessionAppUser) session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		AppUserAttrRef appUserForEdit = new AppUserAttrRef();
		appUserForEdit.setEmail(sessioAppUser.getEmail());
		EditAppUserModAtt manageAppUserModAtt = new EditAppUserModAtt();
		manageAppUserModAtt.setAppUser(appUserForEdit);
		model.addAttribute(Constants.editAppUserModAtt.name(), manageAppUserModAtt);
		return Constants.editAppUserPage.name();

	}

	@RequestMapping(value = "/controlActionAppUser", params= {"submit"}, method = RequestMethod.POST)
	public String submitChanges(//
			@ModelAttribute(name = "editAppUserModAtt")
			EditAppUserModAtt editAppUser//
			, Model model
			, StandardSessionFacade session
	) {
		SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		if (checkFields(editAppUser, sessionAppUser)) {
			AppUserAttrRef appUser = editAppUser.getAppUser();
			// check if email already exists for another AppUser
			try {
				appUserService.updateAppUserByEditAppUser(//
						sessionAppUser.getId()//
						, sessionAppUser.getEmail()//
						, appUser//
						, editAppUser.getPasswordRepeated()//
				);
				sessionAppUser.setEmail(//
						!Strings.isEmpty(appUser.getEmail())//
						? appUser.getEmail()//
						: sessionAppUser.getEmail()//
				);
				return menuController.showMenuPage(model, session);
			} catch (AppUserAlreadyExistsException e) {//
				editAppUser.setAppUserAlreadyExists(true);
			} catch (WrongPasswordException e) {
				editAppUser.setWrongPassword(true);
			}
		}
		return Constants.editAppUserPage.name();
	}

	@RequestMapping(value = "/controlActionAppUser", params= {"cancel"}, method = RequestMethod.POST)
	public String cancel(//
			@ModelAttribute(name = "editAppUserModAtt")
			EditAppUserModAtt editAppUser//
			, Model model//
			, StandardSessionFacade session
	) {
		return menuController.showMenuPage(model, session);
	}
	
	private boolean checkPasswords(EditAppUserModAtt editAppUserModAtt) {
		if (!Strings.isEmpty(editAppUserModAtt.getPasswordRepeated())) {
			if (// 
					!editAppUserModAtt.getAppUser().getPassword().equals(//
							editAppUserModAtt.getPasswordRepeated()//
					)
			) {
				editAppUserModAtt.setPasswordsNotEqual(true);
				return false;
			}
		}
		return true;
	}
	private boolean checkFields(EditAppUserModAtt editAppUserModAtt, SessionAppUser sessionAppUser) {
		if (//
				Strings.isBlank(editAppUserModAtt.getAppUser().getPassword())//
				|| (//
						Strings.isBlank(editAppUserModAtt.getPasswordRepeated())//
						&& (//
								Strings.isEmpty(editAppUserModAtt.getAppUser().getEmail())//
								|| editAppUserModAtt.getAppUser().getEmail().equals(sessionAppUser.getEmail())
						)
				)
		) {
			editAppUserModAtt.setMandatoryViolated(true);
			return false;
		}
		return checkPasswords(editAppUserModAtt);
	}
}
