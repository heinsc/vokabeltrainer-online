package de.heins.vokabeltraineronline.web.controller;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class AnswerIsCorrectController {
	private static enum Constants {
		answerIsCorrectPage
	}
	public AnswerIsCorrectController() {
		super();
	}
	@Autowired
	private LearnDoLearnController LearnDoLearnController;

	public String showAnswerIsCorrectPage() {
		return Constants.answerIsCorrectPage.name();
	}

	@RequestMapping(value = "/controlActionAnswerIsCorrect", method = RequestMethod.POST, params = {"nextQuestion"})
	public String nextQuestion(Model model, StandardSessionFacade session) {
		return LearnDoLearnController.showLearnDoLearnPages(model, session);
	}


}