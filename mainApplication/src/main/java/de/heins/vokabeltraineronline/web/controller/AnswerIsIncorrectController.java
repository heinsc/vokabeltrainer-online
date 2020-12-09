package de.heins.vokabeltraineronline.web.controller;


import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.heins.vokabeltraineronline.web.entities.attributereference.QuestionWithAnswerAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.AnswerIsIncorrectModAtt;


@Controller
public class AnswerIsIncorrectController {
	private static enum Constants {
		answerIsIncorrectPage, answerIsIncorrectModAtt
	}
	public AnswerIsIncorrectController() {
		super();
	}
	@Autowired
	private LearnDoLearnController learnDoLearnController;
	public String showAnswerIsIncorrectPage(//
			Model model//  
			, QuestionWithAnswerAttrRef questionWithAnswerAttrRef//
			, String answerByUser//
	) {
		AnswerIsIncorrectModAtt answerIsIncorrectModAtt = new AnswerIsIncorrectModAtt();
		answerIsIncorrectModAtt.setQuestionWithAnswerAttrRef(questionWithAnswerAttrRef);
		answerIsIncorrectModAtt.setAnswerByUser(answerByUser);
		model.addAttribute(Constants.answerIsIncorrectModAtt.name(), answerIsIncorrectModAtt);
		return Constants.answerIsIncorrectPage.name();

	}

	@RequestMapping(value = "/controlActionAnswerIsIncorrect", method = RequestMethod.POST, params = {"submit"})
	public String submit(//
			Model model//
			, StandardSessionFacade session//
	) {
		return learnDoLearnController.showLearnDoLearnPages(model, session);
	}
}