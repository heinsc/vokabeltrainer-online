package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

import de.heins.vokabeltraineronline.web.entities.attributereference.AppUserAttrRef;

public class LoginModAtt {
	private Boolean mandatoryViolated;
	private Boolean loginError;
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
	public Boolean getLoginError() {
		return loginError;
	}
	public void setLoginError(Boolean loginError) {
		this.loginError = loginError;
	}

}
