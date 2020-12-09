package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

import de.heins.vokabeltraineronline.web.entities.attributereference.QuestionWithAnswerAttrRef;

public class AnswerIsIncorrectModAtt {
	private QuestionWithAnswerAttrRef questionWithAnswerAttrRef;
	private String answerByUser;
	public QuestionWithAnswerAttrRef getQuestionWithAnswerAttrRef() {
		return questionWithAnswerAttrRef;
	}
	public void setQuestionWithAnswerAttrRef(QuestionWithAnswerAttrRef questionWithAnswerAttrRef) {
		this.questionWithAnswerAttrRef = questionWithAnswerAttrRef;
	}
	public String getAnswerByUser() {
		return answerByUser;
	}
	public void setAnswerByUser(String answerByUser) {
		this.answerByUser = answerByUser;
	}


}
