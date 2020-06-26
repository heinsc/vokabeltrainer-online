package de.heins.vokabeltraineronline.web.entities;

public class RegisterForm {
	private Boolean mandatoryViolated;
	private Boolean passwordsNotEqual;
	private Boolean userAdded;
	private Boolean userAlreadyExists;
	private String email;
	private String password;
	private String passwordRepeated;
	public String getPasswordRepeated() {
		return passwordRepeated;
	}
	public void setPasswordRepeated(String passwordRepeated) {
		this.passwordRepeated = passwordRepeated;
	}
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
	public Boolean getUserAdded() {
		return userAdded;
	}
	@Deprecated
	public void setUserAdded(Boolean loginOK) {
		this.userAdded = loginOK;
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
