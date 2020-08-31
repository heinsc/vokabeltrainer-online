package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

import java.util.List;

import de.heins.vokabeltraineronline.web.entities.attributereference.LearningStrategyAttrRef;

public class EditOrCreateLearningStrategyModAtt {
	private LearningStrategyAttrRef learningStrategy;
	private List<String> selectableSuccessSteps;
	private boolean mandatoryViolated;
	private boolean learningStrategyWithThisNameAlreadyExists;
	public LearningStrategyAttrRef getLearningStrategy() {
		return learningStrategy;
	}
	public void setLearningStrategy(LearningStrategyAttrRef learningStrategy) {
		this.learningStrategy = learningStrategy;
	}
	public List<String> getSelectableSuccessSteps() {
		return selectableSuccessSteps;
	}
	public void setSelectableSuccessSteps(List<String> selectableSuccessSteps) {
		this.selectableSuccessSteps = selectableSuccessSteps;
	}
	public boolean isMandatoryViolated() {
		return mandatoryViolated;
	}
	public void setMandatoryViolated(boolean mandatoryViolated) {
		this.mandatoryViolated = mandatoryViolated;
	}
	public boolean isLearningStrategyWithThisNameAlreadyExists() {
		return learningStrategyWithThisNameAlreadyExists;
	}
	public void setLearningStrategyWithThisNameAlreadyExists(boolean learningStrategyWithThisNameAlreadyExists) {
		this.learningStrategyWithThisNameAlreadyExists = learningStrategyWithThisNameAlreadyExists;
	}


}
