package de.heins.vokabeltraineronline.business.entity;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class UserFactory {
	private Long id;
	private String email;
	private String password;
	private Date lastLogin;

	public Long getId() {
		return id;
	}

	public UserFactory setId(Long id) {
		this.id = id;
		return this;
	}

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

	public User getNewObject() {
		// TODO Auto-generated method stub
		return new User(//
				this.id//
				, this.email//
				, this.password//
				, this.lastLogin//
		);
	}

}
