package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

public class LearnFilterIndexBoxesModAtt {
	private Boolean mandatoryViolated;
	private Boolean noQuestionsWithThisSelection;
	public Boolean getMandatoryViolated() {
		return mandatoryViolated;
	}
	public void setMandatoryViolated(Boolean mandatory) {
		this.mandatoryViolated = mandatory;
	}
	public Boolean getNoQuestionsWithThisSelection() {
		return noQuestionsWithThisSelection;
	}
	public void setNoQuestionsWithThisSelection(Boolean noQuestionsWithThisSelection) {
		this.noQuestionsWithThisSelection = noQuestionsWithThisSelection;
	}
}
