package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

import de.heins.vokabeltraineronline.web.entities.attributereference.LearningStrategyAttrRef;

public class DeleteLearningStrategyModAtt {
	private Boolean mandatoryViolated;
	private Boolean wrongPassword;
	private LearningStrategyAttrRef learningStrategy;
	private String password;
	public Boolean getMandatoryViolated() {
		return mandatoryViolated;
	}
	public void setMandatoryViolated(Boolean mandatory) {
		this.mandatoryViolated = mandatory;
	}
	public Boolean getWrongPassword() {
		return wrongPassword;
	}
	public void setWrongPassword(Boolean wrongPassword) {
		this.wrongPassword = wrongPassword;
	}
	public LearningStrategyAttrRef getLearningStrategy() {
		return learningStrategy;
	}
	public void setLearningStrategy(LearningStrategyAttrRef learningStrategy) {
		this.learningStrategy = learningStrategy;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
