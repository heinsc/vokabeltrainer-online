package de.heins.vokabeltrainerbackup.business.entity;

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
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"appUser", "name"})})
public class LearningStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    
    @ManyToMany(fetch = FetchType.EAGER)
	private List<SuccessStep> successSteps;

	@ManyToOne
	@JoinColumn(name="appUser")
	private AppUser appUser;
    public LearningStrategy() {
    }
    public LearningStrategy(//
    		Long id2//
    		, String name//
    		, AppUser appUser
    	) {
    	this.id=id2;
		this.name=name;
		this.appUser=appUser;
		this.successSteps=new LinkedList<SuccessStep>();
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

	public AppUser getAppUser() {
		return appUser;
	}
	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}
	public void addSuccessStep(SuccessStep step) {
		this.successSteps.add(step);
	}
	public void moveSuccessStepUp(SuccessStep step) {
		int indexOfSuccessStep = successSteps.indexOf(step);
		if (indexOfSuccessStep>0) {
			successSteps.remove(indexOfSuccessStep);
			successSteps.add(indexOfSuccessStep-1, step);
		}
	}
	public void moveSuccessStepDown(SuccessStep step) {
		int indexOfSuccessStep  = successSteps.indexOf(step);
		if (indexOfSuccessStep < successSteps.size()-1) {
			successSteps.remove(indexOfSuccessStep);
			successSteps.add(indexOfSuccessStep+1, step);
		}
	}
	public List<SuccessStep> getSuccessSteps() {
		return successSteps;
	}
	
}
