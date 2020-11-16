package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

import de.heins.vokabeltraineronline.web.entities.attributereference.AppUserAttrRef;

public class LearnFilterIndexBoxesModAtt {
	private Boolean mandatoryViolated;
	private AppUserAttrRef appUser;
	public AppUserAttrRef getAppUser() {
		return appUser;
	}
	public void setAppUser(AppUserAttrRef appUser) {
		this.appUser = appUser;
	}
	private String passwordRepeated;
	public String getPasswordRepeated() {
		return passwordRepeated;
	}
	public void setPasswordRepeated(String passwordRepeated) {
		this.passwordRepeated = passwordRepeated;
	}
	public Boolean getMandatoryViolated() {
		return mandatoryViolated;
	}
	public void setMandatoryViolated(Boolean mandatory) {
		this.mandatoryViolated = mandatory;
	}
}
