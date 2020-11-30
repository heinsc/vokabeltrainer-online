package de.heins.vokabeltraineronline.business.entity;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class AppUserFactory {
	private Long id;
	private String email;
	private String password;
	private Date lastLogin;
	private BehaviourIfPoolWithWrongAnswersIsFull behaviourIfPoolWithWrongAnswersIsFull;
	private int maxNumberOfWrongAnswersPerSession;

	public AppUserFactory setId(Long id) {
		this.id = id;
		return this;
	}

	public AppUserFactory setEMail(String email) {
		this.email=email;
		return this;
	}

	public AppUserFactory setPassword(String password) {
		this.password=password;
		return this;
	}

	public AppUserFactory setLastLogin(Date time) {
		this.lastLogin=time;
		return this;
	}

	public AppUser getNewObject() {
		return new AppUser(//
				this.id//
				, this.email//
				, this.password//
				, this.maxNumberOfWrongAnswersPerSession//
				, this.behaviourIfPoolWithWrongAnswersIsFull//
				, this.lastLogin//
		);
	}

	public AppUserFactory setBehaviourIfPoolWithWrongAnswersIsFull(BehaviourIfPoolWithWrongAnswersIsFull behaviourIfPoolWithWrongAnswersIsFull) {
		this.behaviourIfPoolWithWrongAnswersIsFull = behaviourIfPoolWithWrongAnswersIsFull;
		return this;
	}

	public AppUserFactory setMaxNumberOfWrongAnswersPerSession(int maxNumberOfWrongAnswersPerSession) {
		this.maxNumberOfWrongAnswersPerSession = maxNumberOfWrongAnswersPerSession;
		return this;
	}

}
