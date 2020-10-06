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
import de.heins.vokabeltraineronline.business.service.SuccessStepService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.AppUserAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.SuccessStepAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.DeleteSuccessStepModAtt;

@Controller
public class DeleteSuccessStepController {
	private static enum Constants {
		deleteSuccessStepPage,  deleteSuccessStepModAtt
	}
	@Autowired
	private AppUserService appUserService;
	
	@Autowired
	private SuccessStepService successStepService;

	public DeleteSuccessStepController() {
		super();
	}

	@RequestMapping({ "/controlPageDeleteSuccessStep" })
	public String showDeleteSuccessStepPage(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		SessionAppUser sessionAppUser = (SessionAppUser) session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		String sessionOldVersionOfSuccessStepName = (String) session.getAttribute(ControllerConstants.sessionOldVersionOfSuccessStepName.name());
		SuccessStepAttrRef successStep = successStepService.findForAppUserAndName(sessionAppUser, sessionOldVersionOfSuccessStepName);
		
		DeleteSuccessStepModAtt deleteSuccessStepModAtt = new DeleteSuccessStepModAtt();
		deleteSuccessStepModAtt.setSuccessStep(successStep);
		model.addAttribute(//
				Constants.deleteSuccessStepModAtt.name()//
				, deleteSuccessStepModAtt//
		);
		return Constants.deleteSuccessStepPage.name();

	}
	@RequestMapping(value = "/controlActionDeleteSuccessStep", method = RequestMethod.POST, params = {"cancel"})
	public String cancel() {
		return "redirect:" + ControllerConstants.controlPageManageConfigurations.name();
	}
	@RequestMapping(value = "/controlActionDeleteSuccessStep", method = RequestMethod.POST, params = {"delete"})
	public String delete(//
			@ModelAttribute(name = "deleteSuccessStepModAtt")
			DeleteSuccessStepModAtt deleteSuccessStep//
			, HttpSession session
	) {
		SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		AppUserAttrRef appUserAttrRef = new AppUserAttrRef();
		appUserAttrRef.setEmail(sessionAppUser.getEmail());
		appUserAttrRef.setPassword(deleteSuccessStep.getPassword());
		if (checkFields(deleteSuccessStep)) {
			try {
				appUserService.getSessionAppUserForLogin(appUserAttrRef);
				// TODO eigentliches Delete...
				return "redirect:" + ControllerConstants.controlPageManageConfigurations.name();
			} catch (WrongPasswordException e) {
				deleteSuccessStep.setWrongPassword(true);
			}
		}
		return Constants.deleteSuccessStepPage.name();
	}

	private boolean checkFields(DeleteSuccessStepModAtt deleteSuccessStepModAtt) {
		if (//
				Strings.isBlank(deleteSuccessStepModAtt.getPassword())//
		) {
			deleteSuccessStepModAtt.setMandatoryViolated(true);
			
			return false;
		}
		return true;
	}
}
