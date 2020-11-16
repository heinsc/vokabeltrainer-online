package de.heins.vokabeltraineronline.web.controller;

import org.springframework.stereotype.Controller;
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

	@RequestMapping("/controlPageAnswerIsCorrect")
	public String showAnswerIsCorrectPage() throws Exception {
		return Constants.answerIsCorrectPage.name();

	}

	@RequestMapping(value = "/controlActionAnswerIsCorrect", method = RequestMethod.POST, params = {"nextQuestion"})
	public String nextQuestion() throws Exception {
		return "redirect:" + ControllerConstants.controlPageLearnDoLearn.name();

	}
	@RequestMapping(value = "/controlActionAnswerIsCorrect", method = RequestMethod.POST, params = {"logout"})
	public String logout() throws Exception {
		return "redirect:" + ControllerConstants.controlPageLogin.name();

	}


}