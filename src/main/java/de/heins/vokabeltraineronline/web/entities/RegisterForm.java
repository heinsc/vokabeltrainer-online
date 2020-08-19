package de.heins.vokabeltraineronline.web.entities;

public class RegisterForm {
	private Boolean mandatoryViolated;
	private Boolean passwordsNotEqual;
	private Boolean appUserAlreadyExists;
	private Boolean wrongPassword;
	private AppUserForm appUser;
	public AppUserForm getAppUser() {
		return appUser;
	}
	public void setAppUser(AppUserForm appUser) {
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
	public Boolean getAppUserAlreadyExists() {
		return appUserAlreadyExists;
	}
	public void setAppUserAlreadyExists(Boolean loginError) {
		this.appUserAlreadyExists = loginError;
	}
	public Boolean getPasswordsNotEqual() {
		return passwordsNotEqual;
	}
	public void setPasswordsNotEqual(Boolean passwordsNotEqual) {
		this.passwordsNotEqual = passwordsNotEqual;
	}
	public Boolean getWrongPassword() {
		return wrongPassword;
	}
	public void setWrongPassword(Boolean wrongPassword) {
		this.wrongPassword = wrongPassword;
	}


}
