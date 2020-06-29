package de.heins.vokabeltraineronline.web.entities;

public class RegisterForm {
	private Boolean mandatoryViolated;
	private Boolean passwordsNotEqual;
	private Boolean userAlreadyExists;
	private UserForm user;
	public UserForm getUser() {
		return user;
	}
	public void setUser(UserForm user) {
		this.user = user;
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
	public Boolean getUserAlreadyExists() {
		return userAlreadyExists;
	}
	public void setUserAlreadyExists(Boolean loginError) {
		this.userAlreadyExists = loginError;
	}
	public Boolean getPasswordsNotEqual() {
		return passwordsNotEqual;
	}
	public void setPasswordsNotEqual(Boolean passwordsNotEqual) {
		this.passwordsNotEqual = passwordsNotEqual;
	}


}
