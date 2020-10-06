package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

import java.util.List;
import java.util.Set;

import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.QuestionWithAnswerAttrRef;

public class ManageQuestionsWithAnswersModAtt {
	private List<IndexBoxAttrRef> allIndexBoxes;
	private Set<QuestionWithAnswerAttrRef> questionsWithAnswers;
	public List<IndexBoxAttrRef> getAllIndexBoxes() {
		return allIndexBoxes;
	}

	public void setAllIndexBoxes(List<IndexBoxAttrRef> allIndexBoxes) {
		this.allIndexBoxes = allIndexBoxes;
	}

	public Set<QuestionWithAnswerAttrRef> getQuestionsWithAnswers() {
		return questionsWithAnswers;
	}

	public void setQuestionsWithAnswers(Set<QuestionWithAnswerAttrRef> questionsWithAnswersList) {
		this.questionsWithAnswers = questionsWithAnswersList;
	}

}
