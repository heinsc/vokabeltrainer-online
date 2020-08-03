package de.heins.vokabeltraineronline.business.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"name", "user"})})
public class SuccessStep extends UserOwnedObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    
	private int durationOfNextAppearance;
	
	private LearningStrategy learningStrategy;
    public SuccessStep() {
    }
    public SuccessStep(//
    		Long id2//
    		, String name2//
    		, int durationOfNextAppearance2//
    		, LearningStrategy lerLearningStrategy//
    		, User user//
    	) {
    	this.id=id2;
		this.name=name2;
		this.durationOfNextAppearance=durationOfNextAppearance2;
		this.user=user;
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
	public int getDurationOfNextAppearance() {
		return durationOfNextAppearance;
	}
	public void setDurationOfNextAppearance(int durationOfNextAppearance) {
		this.durationOfNextAppearance = durationOfNextAppearance;
	}
	public LearningStrategy getLearningStrategy() {
		return learningStrategy;
	}
	public void setLearningStrategy(LearningStrategy learningStrategy) {
		this.learningStrategy = learningStrategy;
	}


}
