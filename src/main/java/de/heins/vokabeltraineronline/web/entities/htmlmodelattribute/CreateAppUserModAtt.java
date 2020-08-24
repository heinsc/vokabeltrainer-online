package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

import de.heins.vokabeltraineronline.web.entities.attributereference.AppUserAttrRef;

public class CreateAppUserModAtt {
	private Boolean mandatoryViolated;
	private Boolean passwordsNotEqual;
	private Boolean appUserAlreadyExists;
	private Boolean wrongPassword;
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
