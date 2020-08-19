package de.heins.vokabeltraineronline.web.controller;


import java.util.Arrays;
import java.util.List;

import org.apache.catalina.session.StandardSessionFacade;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.heins.vokabeltraineronline.business.entity.BehaviourIfWrong;
import de.heins.vokabeltraineronline.business.service.SuccessStepService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUserForm;
import de.heins.vokabeltraineronline.web.entities.SuccessStepForm;

@Controller
public class EditOrCreateSuccessStepController {
	
	@Autowired
	private SuccessStepService successStepService;
	private String oldVersionOfSuccessStepName;

	public EditOrCreateSuccessStepController() {
		super();
	}

	@RequestMapping({ "/createSuccessStep" })
	public String editSuccessStep(//
			Model model//
			, StandardSessionFacade session//
	) throws Exception {
		oldVersionOfSuccessStepName = "";
		
		SuccessStepForm newSuccessStepForm = new SuccessStepForm();
	    model.addAttribute("successStepForm", newSuccessStepForm);
	    
	    List<BehaviourIfWrong> selectableBehaviours = Arrays.asList(BehaviourIfWrong.values());
	    model.addAttribute("selectableBehaviours", selectableBehaviours);
	    	    
		return "editOrCreateSuccessStep";
	}
	@RequestMapping(value="/editOrCreateSuccessStep", method=RequestMethod.POST, params= {"submit"})
	public String submit(//
			SuccessStepForm successStepForm//
			, StandardSessionFacade session//
	) {
		SessionAppUserForm sessionAppUserForm = (SessionAppUserForm)session.getAttribute("sessionAppUser");
		
		if (Strings.isEmpty(oldVersionOfSuccessStepName) || !oldVersionOfSuccessStepName.equals(successStepForm.getName())) {
			// look for duplicates
			SuccessStepForm fromDataBase = successStepService.findForAppUserAndName(sessionAppUserForm, successStepForm.getName());
			if (SuccessStepService.EMPTY_SUCCESS_STEP != fromDataBase) {
				// handle duplicate new success step
				return "editOrCreateSuccessStep";
			}
		}
		successStepService.update(sessionAppUserForm, successStepForm, oldVersionOfSuccessStepName);
		return "/manageIndexBoxes";
	}
	
	@RequestMapping(value="/editOrCreateSuccessStep", method=RequestMethod.POST, params= {"cancel"})
	public String cancel() {
		return "/manageIndexBoxes";
		
	}

}
