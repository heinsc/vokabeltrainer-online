package de.heins.vokabeltraineronline.web.entities;

public class AuthentificationForm {
	private Boolean mandatoryViolated;
	private Boolean loginOK;
	private Boolean loginError;
	private String email;
	private String password;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String aPassword) {
		this.password = aPassword;
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
