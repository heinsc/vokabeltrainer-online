package de.heins.vokabeltrainerrestorebackup.business.entity.backup;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"appUserBackup", "name"})})
public class LearningStrategyBackup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    
    @ManyToMany(fetch = FetchType.EAGER)
	private List<SuccessStepBackup> successStepBackups;

	@ManyToOne
	@JoinColumn(name="appUserBackup")
	private AppUserBackup appUserBackup;
    public LearningStrategyBackup() {
    }
    public LearningStrategyBackup(//
    		Long id2//
    		, String name//
    		, AppUserBackup appUserBackup
    	) {
    	this.id=id2;
		this.name=name;
		this.appUserBackup=appUserBackup;
		this.successStepBackups=new LinkedList<SuccessStepBackup>();
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

	public AppUserBackup getAppUserBackup() {
		return appUserBackup;
	}
	public void setAppUserBackup(AppUserBackup appUserBackup) {
		this.appUserBackup = appUserBackup;
	}
	public void addSuccessStepBackup(SuccessStepBackup backup) {
		this.successStepBackups.add(backup);
	}
	public void moveSuccessStepBackupUp(SuccessStepBackup backup) {
		int indexOfSuccessStep = successStepBackups.indexOf(backup);
		if (indexOfSuccessStep>0) {
			successStepBackups.remove(indexOfSuccessStep);
			successStepBackups.add(indexOfSuccessStep-1, backup);
		}
	}
	public void moveSuccessStepBackupDown(SuccessStepBackup backup) {
		int indexOfSuccessStep  = successStepBackups.indexOf(backup);
		if (indexOfSuccessStep < successStepBackups.size()-1) {
			successStepBackups.remove(indexOfSuccessStep);
			successStepBackups.add(indexOfSuccessStep+1, backup);
		}
	}
	public List<SuccessStepBackup> getSuccessStepBackups() {
		return successStepBackups;
	}
	
}
