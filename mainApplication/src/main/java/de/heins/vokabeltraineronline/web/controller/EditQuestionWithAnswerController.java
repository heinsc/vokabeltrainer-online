package de.heins.vokabeltraineronline.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.catalina.session.StandardSessionFacade;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.heins.vokabeltraineronline.business.service.IndexBoxService;
import de.heins.vokabeltraineronline.business.service.QuestionWithAnswerService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.QuestionWithAnswerAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.EditQuestionWithAnswerModAtt;

@Controller
public class EditQuestionWithAnswerController {
	private static enum Constants {
		editQuestionWithAnswerPage//
		, editQuestionWithAnswerModAtt//
		, sessionOldVersionOfQuestion
	}
	@Autowired
	private ManageQuestionsWithAnswersController manageQuestionsWithAnswersController;
	@Autowired
	private IndexBoxService indexBoxService;
	
	@Autowired
	private QuestionWithAnswerService questionWithAnswerService;
	
	public EditQuestionWithAnswerController() {
		super();
	}

	public String showEditQuestionWithAnswerPage(//
			QuestionWithAnswerAttrRef questionWithAnswerAttrRef//
			, Model model//
			, StandardSessionFacade session//
	) {
		EditQuestionWithAnswerModAtt editQuestionWithAnswerModAtt = new EditQuestionWithAnswerModAtt();
		editQuestionWithAnswerModAtt.setQuestionWithAnswer(questionWithAnswerAttrRef);
		SessionAppUser  sessionAppUser = (SessionAppUser) session.getAttribute(ControllerConstants.sessionAppUser.name());
		refreshSelectBoxes(sessionAppUser, editQuestionWithAnswerModAtt);
		
		session.setAttribute(Constants.sessionOldVersionOfQuestion.name(), questionWithAnswerAttrRef.getQuestion());
		model.addAttribute(Constants.editQuestionWithAnswerModAtt.name(), editQuestionWithAnswerModAtt);
		
		return Constants.editQuestionWithAnswerPage.name();
	}

	private void refreshIndexBoxDescriptions(//
			EditQuestionWithAnswerModAtt editQuestionWithAnswerModAtt//
			, SessionAppUser sessionAppUser//
	) {
		List<IndexBoxAttrRef> indexBoxes = indexBoxService.findAllForAppUser(sessionAppUser);
		editQuestionWithAnswerModAtt.setNoIndexBoxExists(indexBoxes.size() == 0);
		List<String> localIndexBoxDescriptions = indexBoxes.stream().map(
				currentIndexBox -> 
					// the structure of indexBoxDescription is important for adding or editing 
					// QuestionsWithAnswerService#update
					currentIndexBox.getName()//
					+ QuestionWithAnswerService.INDEXBOX_DESCRIPTION_SPLITTER//
					+ currentIndexBox.getSubject()
		).collect(Collectors.toList());
		editQuestionWithAnswerModAtt.setIndexBoxesDescriptions(
				localIndexBoxDescriptions
		);
		QuestionWithAnswerAttrRef questionWithAnswer = editQuestionWithAnswerModAtt//
		.getQuestionWithAnswer();
		if (questionWithAnswer != null) {
			editQuestionWithAnswerModAtt.setSelectedIndexBoxIndex(//
					localIndexBoxDescriptions.indexOf(//
						questionWithAnswer//
						.getIndexBoxDescription()//
				)//
			);
		} else {
			editQuestionWithAnswerModAtt.setSelectedIndexBoxIndex(-1);
		}

	}

	@RequestMapping(value="/controlActionEditQuestionWithAnswer", method=RequestMethod.POST, params= {"cancel"})
	public String cancel(Model model, StandardSessionFacade session) {
		return manageQuestionsWithAnswersController.showManageQuestionsWithAnswersPage(model, session);
		
	}
	private void refreshSelectBoxes(//
			SessionAppUser sessionAppUser//
			, EditQuestionWithAnswerModAtt editQuestionWithAnswerModAtt
	) {
		refreshIndexBoxDescriptions(//
				editQuestionWithAnswerModAtt
				, sessionAppUser//
		);
	}
	@RequestMapping(value="/controlActionEditQuestionWithAnswer", method=RequestMethod.POST, params= {"submit"})
	public String submit(
			Model model//
			, StandardSessionFacade session//
			, @ModelAttribute(name = "editQuestionWithAnswerModAtt")
			EditQuestionWithAnswerModAtt editQuestionWithAnswerModAtt
	) {
		String returnValue = trySaveQuestionWithAnswer(session, editQuestionWithAnswerModAtt);
		if (!Strings.isEmpty(returnValue)) {
			return returnValue;
		}
		return manageQuestionsWithAnswersController.showManageQuestionsWithAnswersPage(model, session);
	}

	private String trySaveQuestionWithAnswer(//
			StandardSessionFacade session//
			, EditQuestionWithAnswerModAtt editQuestionWithAnswerModAtt//
	) {
		SessionAppUser sessionAppUser = (SessionAppUser) session.getAttribute(ControllerConstants.sessionAppUser.name());
		editQuestionWithAnswerModAtt.setMandatoryViolated(false);
		editQuestionWithAnswerModAtt.setQuestionAlreadyExists(false);
		editQuestionWithAnswerModAtt.setUnallowedSubstring(false);
		refreshSelectBoxes(sessionAppUser, editQuestionWithAnswerModAtt);
		if (Strings.isEmpty(editQuestionWithAnswerModAtt.getQuestionWithAnswer().getQuestion())) {
			editQuestionWithAnswerModAtt.setMandatoryViolated(true);
			return Constants.editQuestionWithAnswerPage.name();
		}
		if (editQuestionWithAnswerModAtt//
				.getQuestionWithAnswer()//
				.getQuestion()//
				.indexOf(";,;") > 0//\
			|| editQuestionWithAnswerModAtt//
				.getQuestionWithAnswer()//
				.getAnswer()//
				.indexOf(";,;") > 0//
		) {
			editQuestionWithAnswerModAtt.setUnallowedSubstring(true);
			return Constants.editQuestionWithAnswerPage.name();
		}
		// in comparison to de.heins.vokabeltraineronline.web.controller.EditOrCreateSuccessStepController.submit(EditOrCreateSuccessStepModAttr, StandardSessionFacade, Model)
		// here the instance is always created. So the if clause for oldQuestion is not needed.
		// the check for duplicates has to be done in every case
		QuestionWithAnswerAttrRef fromDataBase = questionWithAnswerService.findForAppUserAndQuestion(//
				sessionAppUser//
				, editQuestionWithAnswerModAtt.getQuestionWithAnswer().getQuestion()
		);
		if (QuestionWithAnswerService.EMPTY_QUESTION_WITH_ANSWER != fromDataBase) {
			editQuestionWithAnswerModAtt.setQuestionAlreadyExists(true);
			return Constants.editQuestionWithAnswerPage.name();
		}
		String oldVersionOfQuestion = (String) session.getAttribute(Constants.sessionOldVersionOfQuestion.name());
		questionWithAnswerService.update(//
				sessionAppUser//
				, editQuestionWithAnswerModAtt.getQuestionWithAnswer()//
				, oldVersionOfQuestion//
		);
		return "";
	}

}
