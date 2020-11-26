package de.heins.vokabeltrainerdropmaindatabase.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    
	private String password;
    
    private Date lastLogin;
    
    private FaultTolerance faultTolerance;
    
	/*
	 * Remarks on the two properties behaviourIfPoolWithWrongAnswersIsFull and
	 * maxNumberOfWrongAnswersPerSession They could be outsourced into a separate
	 * class "LearningProfile". But the object model grew to complicated then. But,
	 * on user frontend side, these two properties are managed separately in
	 * "createOrEditLearningProfile" while the other attributes are managed in
	 * "manageAccount"
	 */
	private BehaviourIfPoolWithWrongAnswersIsFull behaviourIfPoolWithWrongAnswersIsFull;

	private int maxNumberOfWrongAnswersPerSession;
    public AppUser() {
    }
    public AppUser(//
    		Long id2, String email2//
    		, String password2//
    		, FaultTolerance faultTolerance
    		, int maxNumberOfWrongAnswersPerSession//
    		, BehaviourIfPoolWithWrongAnswersIsFull behaviourIfPoolWithWrongAnswersIsFull//
    		, Date lastLogin2//
    	) {
    	this.id=id2;
		this.email=email2;
		this.password=password2;
		this.faultTolerance=faultTolerance;
		this.maxNumberOfWrongAnswersPerSession=maxNumberOfWrongAnswersPerSession;
		this.behaviourIfPoolWithWrongAnswersIsFull=behaviourIfPoolWithWrongAnswersIsFull;
		this.lastLogin=lastLogin2;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public BehaviourIfPoolWithWrongAnswersIsFull getBehaviourIfPoolWithWrongAnswersIsFull() {
		return behaviourIfPoolWithWrongAnswersIsFull;
	}
	public int getMaxNumberOfWrongAnswersPerSession() {
		return maxNumberOfWrongAnswersPerSession;
	}
	public void setBehaviourIfPoolWithWrongAnswersIsFull(BehaviourIfPoolWithWrongAnswersIsFull behaviourIfPoolWithWrongAnswersIsFull) {
		this.behaviourIfPoolWithWrongAnswersIsFull = behaviourIfPoolWithWrongAnswersIsFull;
	}
	public void setMaxNumberOfWrongAnswersPerSession(int maxNumberOfWrongAnswersPerSession) {
		this.maxNumberOfWrongAnswersPerSession = maxNumberOfWrongAnswersPerSession;
	}
	public FaultTolerance getFaultTolerance() {
		return faultTolerance;
	}
	public void setFaultTolerance(FaultTolerance faultTolerance) {
		this.faultTolerance = faultTolerance;
	}


}
