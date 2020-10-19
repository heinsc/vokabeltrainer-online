package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

import java.util.List;

import de.heins.vokabeltraineronline.web.entities.attributereference.QuestionWithAnswerAttrRef;

public class CreateQuestionsWithAnswersModAtt {
	private Boolean mandatoryViolated;
	private Boolean questionAlreadyExists;
	private Boolean noIndexBoxExists;
	private List<String> indexBoxesDescriptions; 
	private List<String> learningStrategiesDescriptions;
	private QuestionWithAnswerAttrRef questionWithAnswer;
	public Boolean getMandatoryViolated() {
		return mandatoryViolated;
	}
	public void setMandatoryViolated(Boolean mandatory) {
		this.mandatoryViolated = mandatory;
	}
	public Boolean getQuestionAlreadyExists() {
		return questionAlreadyExists;
	}
	public void setQuestionAlreadyExists(Boolean questionAlreadyExists) {
		this.questionAlreadyExists = questionAlreadyExists;
	}
	public Boolean getNoIndexBoxExists() {
		return noIndexBoxExists;
	}
	public void setNoIndexBoxExists(Boolean noIndexBoxExists) {
		this.noIndexBoxExists = noIndexBoxExists;
	}
	public QuestionWithAnswerAttrRef getQuestionWithAnswer() {
		return questionWithAnswer;
	}
	public void setQuestionWithAnswer(QuestionWithAnswerAttrRef questionWithAnswer) {
		this.questionWithAnswer = questionWithAnswer;
	}
	public List<String> getIndexBoxesDescriptions() {
		return indexBoxesDescriptions;
	}
	public void setIndexBoxesDescriptions(List<String> indexBoxesDescriptions) {
		this.indexBoxesDescriptions = indexBoxesDescriptions;
	}
	public List<String> getLearningStrategiesDescriptions() {
		return learningStrategiesDescriptions;
	}
	public void setLearningStrategiesDescriptions(List<String> learningStrategiesDiscriptions) {
		this.learningStrategiesDescriptions = learningStrategiesDiscriptions;
	}


}
