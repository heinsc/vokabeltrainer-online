package de.heins.vokabeltraineronline.web.entities;

import java.util.List;

import de.heins.vokabeltraineronline.web.entities.attributereference.LearningStrategyAttrRef;

public class SessionEditOrCreateLearningStrategy {
	private LearningStrategyAttrRef learningStrategy;
	private List<String> selectableSuccessSteps;
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


}
