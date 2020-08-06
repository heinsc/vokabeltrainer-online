package de.heins.vokabeltraineronline.business.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"name", "user"})})
public class SuccessStep {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    
	private int nextAppearanceInDays;
	
	@ManyToOne
	private LearningStrategy learningStrategy;
	
	private BehaviourIfWrong behaviourIfWrong;

	@ManyToOne
	@JoinColumn(name="user")
	private User user;
	
    public SuccessStep() {
    }
    public SuccessStep(//
    		Long id2//
    		, String name2//
    		, int nextAppearanceInDays2//
    		, LearningStrategy lerLearningStrategy//
    		, BehaviourIfWrong behaviourIfWrong//
    		, User user//
    	) {
    	this.id=id2;
		this.name=name2;
		this.nextAppearanceInDays=nextAppearanceInDays2;
		this.behaviourIfWrong=behaviourIfWrong;
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
	public int getNextAppearanceInDays() {
		return nextAppearanceInDays;
	}
	public void setNextAppearanceInDays(int durationOfNextAppearance) {
		this.nextAppearanceInDays = durationOfNextAppearance;
	}
	public LearningStrategy getLearningStrategy() {
		return learningStrategy;
	}
	public void setLearningStrategy(LearningStrategy learningStrategy) {
		this.learningStrategy = learningStrategy;
	}
	public BehaviourIfWrong getBehaviourIfWrong() {
		return behaviourIfWrong;
	}
	public void setBehaviourIfWrong(BehaviourIfWrong behaviourIfWrong) {
		this.behaviourIfWrong = behaviourIfWrong;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}


}
