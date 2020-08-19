package de.heins.vokabeltraineronline.web.entities;

public class AuthentificationForm {
	private Boolean mandatoryViolated;
	private Boolean loginError;
	private AppUserForm appUser;
	public AppUserForm getAppUser() {
		return appUser;
	}
	public void setAppUser(AppUserForm appUser) {
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
