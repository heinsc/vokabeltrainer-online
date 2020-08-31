package de.heins.vokabeltraineronline.web.controller;



import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.session.StandardSessionFacade;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.heins.vokabeltraineronline.business.service.LearningStrategyService;
import de.heins.vokabeltraineronline.business.service.SuccessStepService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.SessionEditOrCreateLearningStrategy;
import de.heins.vokabeltraineronline.web.entities.attributereference.LearningStrategyAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.SuccessStepAttrRef;

@Controller
public class EditOrCreateLearningStrategyController {
	private static enum Constants {
		editOrCreateLearningStrategyPage//
		, sessionEditOrCreateLearningStrategy//
		, editOrCreateLearningStrategyModAtt
	}
	
	@Autowired
	private LearningStrategyService learningStrategyService;
	@Autowired
	private SuccessStepService successStepService;

	public EditOrCreateLearningStrategyController() {
		super();
	}

	@RequestMapping({ "/controlEditOrCreateLearningStrategy" })
	public String showEditOrCreateLearningStrategyPage(//
			StandardSessionFacade session//
			, Model model//
	) throws Exception {
		
		SessionAppUser sessionAppUser = (SessionAppUser) session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		SessionEditOrCreateLearningStrategy sesseionEditOrCreateLearningStrategy = new SessionEditOrCreateLearningStrategy();
		session.setAttribute(//
				Constants.sessionEditOrCreateLearningStrategy.name()//
				, sesseionEditOrCreateLearningStrategy//
		);
		String oldVersionOfLearningStrategyName = (String) session.getAttribute(//
				ControllerConstants.sessionOldVersionOfLearningStrategyName.name()//
		);
		if (Strings.isEmpty(oldVersionOfLearningStrategyName)) {
			sesseionEditOrCreateLearningStrategy.setLearningStrategy(new LearningStrategyAttrRef());
		} else {
			sesseionEditOrCreateLearningStrategy.setLearningStrategy(//
					learningStrategyService.findForAppUserAndName(sessionAppUser, oldVersionOfLearningStrategyName)//
			);
		}
	    List<SuccessStepAttrRef> selectableSuccessSteps = successStepService.findAllForAppUser(sessionAppUser);
	    sesseionEditOrCreateLearningStrategy.setSelectableSuccessSteps(new ArrayList<String>());
	    selectableSuccessSteps.forEach(//
	    		currentStep -> sesseionEditOrCreateLearningStrategy//
	    			.getSelectableSuccessSteps()//
	    			.add(currentStep.getName())//
	    );
	    model.addAttribute(Constants.editOrCreateLearningStrategyModAtt.name(), sesseionEditOrCreateLearningStrategy);
		return Constants.editOrCreateLearningStrategyPage.name();
	}
	@RequestMapping(value="/controlActionEditOrCreateLearningStrategy", method=RequestMethod.POST, params= {"submit"})
	public String submit(//
			StandardSessionFacade session//
	) {
		SessionAppUser sessionAppUserForm = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		SessionEditOrCreateLearningStrategy sessionEditOrCreateLearningStrategy//
			= (SessionEditOrCreateLearningStrategy) session.getAttribute(//
					Constants.sessionEditOrCreateLearningStrategy.name()//
			);
		String oldVersionOfLearningStrategyName = (String) session.getAttribute(//
				ControllerConstants.sessionOldVersionOfLearningStrategyName.name()//
				);
		if (//
				Strings.isEmpty(oldVersionOfLearningStrategyName)//
				|| !oldVersionOfLearningStrategyName.equals(//
						sessionEditOrCreateLearningStrategy.getLearningStrategy().getName())//
			) {
			// look for duplicates
			LearningStrategyAttrRef fromDataBase = learningStrategyService.findForAppUserAndName(//
					sessionAppUserForm//
					, sessionEditOrCreateLearningStrategy.getLearningStrategy().getName()//
			);
			if (LearningStrategyService.EMPTY_LEARNING_STRATEGY != fromDataBase) {
				// handle duplicate new learning strategy
				return Constants.editOrCreateLearningStrategyPage.name();
			}
		}
		learningStrategyService.update(//
				sessionAppUserForm//
				, sessionEditOrCreateLearningStrategy.getLearningStrategy()//
				, oldVersionOfLearningStrategyName//
		);
		return "redirect:" + ControllerConstants.controlManageConfigurations.name();
	}
	
	@RequestMapping(value="/controlActionEditOrCreateLearningStrategy", method=RequestMethod.POST, params= {"cancel"})
	public String cancel() {
		return "redirect:" + ControllerConstants.controlManageConfigurations.name();
		
	}
	@RequestMapping({"controlLinkAddSuccessStep"})
	public String addSuccessStep(//
            @RequestParam(name = "name", required = false, defaultValue = "")
            String name//
            , StandardSessionFacade session//
            , Model model//
	) throws Exception {
		SessionEditOrCreateLearningStrategy sessionEditOrCreateLearningStrategy//
		= (SessionEditOrCreateLearningStrategy) session.getAttribute(//
				Constants.sessionEditOrCreateLearningStrategy.name()//
		);
		sessionEditOrCreateLearningStrategy//
			.getLearningStrategy()//
			.getAssignedSuccessSteps()//
			.add(name);
	    model.addAttribute(Constants.editOrCreateLearningStrategyModAtt.name(), sessionEditOrCreateLearningStrategy);
		return Constants.editOrCreateLearningStrategyPage.name();
	}
	@RequestMapping({"controlLinkRemoveSuccessStep"})
	public String removeSuccessStep(//
			StandardSessionFacade session//
           ,  @RequestParam(name = "index", required = false, defaultValue = "")
            int index//
           , Model model//
	) throws Exception {
		SessionEditOrCreateLearningStrategy sessionEditOrCreateLearningStrategy//
		= (SessionEditOrCreateLearningStrategy) session.getAttribute(//
				Constants.sessionEditOrCreateLearningStrategy.name()//
		);
		sessionEditOrCreateLearningStrategy.getLearningStrategy().getAssignedSuccessSteps().remove(index);
	    model.addAttribute(Constants.editOrCreateLearningStrategyModAtt.name(), sessionEditOrCreateLearningStrategy);
		return Constants.editOrCreateLearningStrategyPage.name();
	}
	@RequestMapping({"controlLinkMoveUpSuccessStep"})
	public String moveUpSuccessStep(//
			StandardSessionFacade session//
	        ,  @RequestParam(name = "index", required = false, defaultValue = "")
            int index//
            , Model model//
	) throws Exception {
		SessionEditOrCreateLearningStrategy sessionEditOrCreateLearningStrategy//
		= (SessionEditOrCreateLearningStrategy) session.getAttribute(//
				Constants.sessionEditOrCreateLearningStrategy.name()//
		);
		if (index>0) {
			String successStepToMoveUp = sessionEditOrCreateLearningStrategy//
					.getLearningStrategy()//
					.getAssignedSuccessSteps()//
					.remove(index);
			sessionEditOrCreateLearningStrategy//
					.getLearningStrategy()//
					.getAssignedSuccessSteps()//
					.add(index - 1, successStepToMoveUp);
		}
	    model.addAttribute(Constants.editOrCreateLearningStrategyModAtt.name(), sessionEditOrCreateLearningStrategy);
		return Constants.editOrCreateLearningStrategyPage.name();
	}
	@RequestMapping({"controlLinkMoveDownSuccessStep"})
	public String moveDownSuccessStep(//
			StandardSessionFacade session//
	        ,  @RequestParam(name = "index", required = false, defaultValue = "")
            int index//
            , Model model//
	) throws Exception {
		SessionEditOrCreateLearningStrategy sessionEditOrCreateLearningStrategy//
		= (SessionEditOrCreateLearningStrategy) session.getAttribute(//
				Constants.sessionEditOrCreateLearningStrategy.name()//
		);
		List<String> assignedSuccessSteps = sessionEditOrCreateLearningStrategy.getLearningStrategy().getAssignedSuccessSteps();
		if (index<assignedSuccessSteps.size()-1) {
			String successStepToMoveUp = assignedSuccessSteps//
					.remove(index);
			assignedSuccessSteps.add(index + 1, successStepToMoveUp);
		}
	    model.addAttribute(Constants.editOrCreateLearningStrategyModAtt.name(), sessionEditOrCreateLearningStrategy);
		return Constants.editOrCreateLearningStrategyPage.name();
	}


}
