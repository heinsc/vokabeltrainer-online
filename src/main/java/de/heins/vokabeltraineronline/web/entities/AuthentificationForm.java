package de.heins.vokabeltraineronline.web.entities;

public class AuthentificationForm {
	private Boolean mandatoryViolated;
	private Boolean loginOK;
	private Boolean loginError;
	private UserForm user;
	public UserForm getUser() {
		return user;
	}
	public void setUser(UserForm user) {
		this.user = user;
	}
	public Boolean getMandatoryViolated() {
		return mandatoryViolated;
	}
	public void setMandatoryViolated(Boolean mandatory) {
		this.mandatoryViolated = mandatory;
	}
	public Boolean getLoginOK() {
		return loginOK;
	}
	public void setLoginOK(Boolean loginOK) {
		this.loginOK = loginOK;
	}
	public Boolean getLoginError() {
		return loginError;
	}
	public void setLoginError(Boolean loginError) {
		this.loginError = loginError;
	}

}
