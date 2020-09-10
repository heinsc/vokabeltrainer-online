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
import de.heins.vokabeltraineronline.business.service.LearningStrategyService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.AppUserAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.LearningStrategyAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.DeleteLearningStrategyModAtt;

@Controller
public class DeleteLearningStrategyController {
	private static enum Constants {
		deleteLearningStrategyPage//
		,  deleteLearningStrategyModAtt
	}
	@Autowired
	private AppUserService appUserService;
	
	@Autowired
	private LearningStrategyService LearningStrategyService;

	public DeleteLearningStrategyController() {
		super();
	}

	@RequestMapping({ "/controlDeleteLearningStrategy" })
	public String showDeleteLearningStrategyPage(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		SessionAppUser sessionAppUser = (SessionAppUser) session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		String sessionOldVersionOfLearningStrategyName = (String) session.getAttribute(ControllerConstants.sessionOldVersionOfLearningStrategyName.name());
		LearningStrategyAttrRef learningStrategy = LearningStrategyService.findForAppUserAndName(sessionAppUser, sessionOldVersionOfLearningStrategyName);
		
		DeleteLearningStrategyModAtt deleteLearningStrategyModAtt = new DeleteLearningStrategyModAtt();
		deleteLearningStrategyModAtt.setLearningStrategy(learningStrategy);
		model.addAttribute(//
				Constants.deleteLearningStrategyModAtt.name()//
				, deleteLearningStrategyModAtt//
		);
		return Constants.deleteLearningStrategyPage.name();

	}
	@RequestMapping(value = "/controlActionDeleteLearningStrategy", method = RequestMethod.POST, params = {"cancel"})
	public String cancel() {
		return "redirect:" + ControllerConstants.controlManageConfigurations.name();
	}
	@RequestMapping(value = "/controlActionDeleteLearningStrategy", method = RequestMethod.POST, params = {"delete"})
	public String delete(//
			@ModelAttribute(name = "deleteLearningStrategyModAtt")
			DeleteLearningStrategyModAtt learningStrategyModAtt//
			, HttpSession session
	) {
		SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		AppUserAttrRef appUserAttrRef = new AppUserAttrRef();
		appUserAttrRef.setEmail(sessionAppUser.getEmail());
		appUserAttrRef.setPassword(learningStrategyModAtt.getPassword());
		if (checkFields(learningStrategyModAtt)) {
			try {
				appUserService.getSessionAppUserForLogin(appUserAttrRef);
				// TODO eigentliches Delete...
				return "redirect:" + ControllerConstants.controlManageConfigurations.name();
			} catch (WrongPasswordException e) {
				learningStrategyModAtt.setWrongPassword(true);
			}
		}
		return Constants.deleteLearningStrategyModAtt.name();
	}

	private boolean checkFields(DeleteLearningStrategyModAtt deleteLearningStrategyModAtt) {
		if (//
				Strings.isBlank(deleteLearningStrategyModAtt.getPassword())//
		) {
			deleteLearningStrategyModAtt.setMandatoryViolated(true);
			
			return false;
		}
		return true;
	}
}
