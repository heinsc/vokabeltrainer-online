package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

import de.heins.vokabeltraineronline.web.entities.attributereference.SuccessStepAttrRef;

public class DeleteSuccessStepModAtt {
	private Boolean mandatoryViolated;
	private Boolean wrongPassword;
	private SuccessStepAttrRef successStep;
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
	public SuccessStepAttrRef getSuccessStep() {
		return successStep;
	}
	public void setSuccessStep(SuccessStepAttrRef successStep) {
		this.successStep = successStep;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
