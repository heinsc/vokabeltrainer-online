package de.heins.vokabeltraineronline.business.entity;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class UserFactory {
	private String email;
	private String password;
	private Date lastLogin;

	public UserFactory setEMail(String email) {
		this.email=email;
		return this;
	}

	public UserFactory setPassword(String password) {
		this.password=password;
		return this;
	}

	public UserFactory setLastLogin(Date time) {
		this.lastLogin=time;
		return this;
	}

	public Student getNewObject() {
		// TODO Auto-generated method stub
		return new Student(//
				this.email//
				, this.password//
				, this.lastLogin//
		);
	}

}
