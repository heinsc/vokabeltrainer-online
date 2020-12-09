package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;

public class DeleteIndexBoxModAtt {
	private Boolean mandatoryViolated;
	private Boolean wrongPassword;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
