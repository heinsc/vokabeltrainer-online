package de.heins.vokabeltraineronline.web.entities;

public class AppUserForm {
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
}
