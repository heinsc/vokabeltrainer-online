package de.heins.vokabeltraineronline.business.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"user", "name"})})
public class LearningStrategy extends UserOwnedObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    
    private int maxNumberOfWrongAnswersPerSession;
    
    private BehaviourIfPoolWithWrongAnswersIsFull behaviourIfPoolWithWrongAnswersIsFull;
    
	private List<SuccessStep> successSteps;
    public LearningStrategy() {
    }
    public LearningStrategy(//
    		Long id2//
    		, String name//
    		, int maxNumberOfWrongAnswersPerSession//
    		, BehaviourIfPoolWithWrongAnswersIsFull behaviourIfPoolWithWrongAnswersIsFull//
    		, User user
    	) {
    	this.id=id2;
		this.name=name;
		this.maxNumberOfWrongAnswersPerSession=maxNumberOfWrongAnswersPerSession;
		this.behaviourIfPoolWithWrongAnswersIsFull=behaviourIfPoolWithWrongAnswersIsFull;
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

	public int getMaxNumberOfWrongAnswersPerSession() {
		return maxNumberOfWrongAnswersPerSession;
	}
	public void setMaxNumberOfWrongAnswersPerSession(int maxNumberOfWrongAnswersPerSession) {
		this.maxNumberOfWrongAnswersPerSession = maxNumberOfWrongAnswersPerSession;
	}
	public BehaviourIfPoolWithWrongAnswersIsFull getBehaviourIfPoolWithWrongAnswersIsFull() {
		return behaviourIfPoolWithWrongAnswersIsFull;
	}
	public void setBehaviourIfPoolWithWrongAnswersIsFull(BehaviourIfPoolWithWrongAnswersIsFull behaviourIfPoolWithWrongAnswersIsFull) {
		this.behaviourIfPoolWithWrongAnswersIsFull = behaviourIfPoolWithWrongAnswersIsFull;
	}
	public void addSuccessStep(SuccessStep step) {
		this.successSteps.add(step);
	}
	
}
