package de.heins.vokabeltrainerbackup.business.entity.backup;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AppUserBackup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    
	private String password;
    
    private Date lastLogin;
    
    private FaultToleranceBackup faultToleranceBackup;
    
	/*
	 * Remarks on the two properties behaviourIfPoolWithWrongAnswersIsFullBackup and
	 * maxNumberOfWrongAnswersPerSession They could be outsourced into a separate
	 * class "LearningProfile". But the object model grew to complicated then. But,
	 * on user frontend side, these two properties are managed separately in
	 * "createOrEditLearningProfile" while the other attributes are managed in
	 * "manageAccount"
	 */
	private BehaviourIfPoolWithWrongAnswersIsFullBackup behaviourIfPoolWithWrongAnswersIsFullBackup;

	private int maxNumberOfWrongAnswersPerSession;
    public AppUserBackup() {
    }
    public AppUserBackup(//
    		Long id2, String email2//
    		, String password2//
    		, FaultToleranceBackup faultToleranceBackup
    		, int maxNumberOfWrongAnswersPerSession//
    		, BehaviourIfPoolWithWrongAnswersIsFullBackup behaviourIfPoolWithWrongAnswersIsFullBackup//
    		, Date lastLogin2//
    	) {
    	this.id=id2;
		this.email=email2;
		this.password=password2;
		this.faultToleranceBackup=faultToleranceBackup;
		this.maxNumberOfWrongAnswersPerSession=maxNumberOfWrongAnswersPerSession;
		this.behaviourIfPoolWithWrongAnswersIsFullBackup=behaviourIfPoolWithWrongAnswersIsFullBackup;
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
	public BehaviourIfPoolWithWrongAnswersIsFullBackup getBehaviourIfPoolWithWrongAnswersIsFullBackup() {
		return behaviourIfPoolWithWrongAnswersIsFullBackup;
	}
	public int getMaxNumberOfWrongAnswersPerSession() {
		return maxNumberOfWrongAnswersPerSession;
	}
	public void setBehaviourIfPoolWithWrongAnswersIsFullBackup(BehaviourIfPoolWithWrongAnswersIsFullBackup behaviourIfPoolWithWrongAnswersIsFullBackup) {
		this.behaviourIfPoolWithWrongAnswersIsFullBackup = behaviourIfPoolWithWrongAnswersIsFullBackup;
	}
	public void setMaxNumberOfWrongAnswersPerSession(int maxNumberOfWrongAnswersPerSession) {
		this.maxNumberOfWrongAnswersPerSession = maxNumberOfWrongAnswersPerSession;
	}
	public FaultToleranceBackup getFaultToleranceBackup() {
		return faultToleranceBackup;
	}
	public void setFaultToleranceBackup(FaultToleranceBackup faultToleranceBackup) {
		this.faultToleranceBackup = faultToleranceBackup;
	}


}
