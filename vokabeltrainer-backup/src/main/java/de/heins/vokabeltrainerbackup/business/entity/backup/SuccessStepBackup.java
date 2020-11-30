package de.heins.vokabeltrainerbackup.business.entity.backup;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"name", "appUserBackup"})})
public class SuccessStepBackup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    
	private int nextAppearanceInDays;
	
	private BehaviourIfWrongBackup behaviourIfWrongBackup;

	@ManyToOne
	@JoinColumn(name="appUserBackup")
	private AppUserBackup appUserBackup;

	private FaultToleranceBackup faultToleranceBackup;
	
    public SuccessStepBackup() {
    }
    public SuccessStepBackup(//
    		Long id2//
    		, String name2//
    		, FaultToleranceBackup faultToleranceBackup//
    		, int nextAppearanceInDays2//
    		, BehaviourIfWrongBackup behaviourIfWrongBackup//
    		, AppUserBackup appUserBackup//
    	) {
    	this.id=id2;
		this.name=name2;
		this.faultToleranceBackup=faultToleranceBackup;
		this.nextAppearanceInDays=nextAppearanceInDays2;
		this.behaviourIfWrongBackup=behaviourIfWrongBackup;
		this.appUserBackup=appUserBackup;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNextAppearanceInDays() {
		return nextAppearanceInDays;
	}
	public void setNextAppearanceInDays(int durationOfNextAppearance) {
		this.nextAppearanceInDays = durationOfNextAppearance;
	}
	public BehaviourIfWrongBackup getBehaviourIfWrongBackup() {
		return behaviourIfWrongBackup;
	}
	public void setBehaviourIfWrongBackup(BehaviourIfWrongBackup behaviourIfWrongBackup) {
		this.behaviourIfWrongBackup = behaviourIfWrongBackup;
	}
	public AppUserBackup getAppUserBackup() {
		return appUserBackup;
	}
	public void setAppUserBackup(AppUserBackup appUserBackup) {
		this.appUserBackup = appUserBackup;
	}
	public FaultToleranceBackup getFaultToleranceBackup() {
		return faultToleranceBackup;
	}
	public void setFaultToleranceBackup(FaultToleranceBackup faultToleranceBackup) {
		this.faultToleranceBackup = faultToleranceBackup;
	}


}
