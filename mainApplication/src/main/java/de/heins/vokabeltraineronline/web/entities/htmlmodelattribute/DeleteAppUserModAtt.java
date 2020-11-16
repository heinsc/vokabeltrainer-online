package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

import de.heins.vokabeltraineronline.web.entities.attributereference.AppUserAttrRef;

public class DeleteAppUserModAtt {
	private Boolean mandatoryViolated;
	private Boolean wrongPassword;
	private AppUserAttrRef appUser;
	public AppUserAttrRef getAppUser() {
		return appUser;
	}
	public void setAppUser(AppUserAttrRef appUser) {
		this.appUser = appUser;
	}
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
}
