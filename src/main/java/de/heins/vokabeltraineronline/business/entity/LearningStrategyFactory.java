package de.heins.vokabeltraineronline.business.entity;

import org.springframework.stereotype.Component;

@Component
public class LearningStrategyFactory {
    private Long id;

    private String name;
    private AppUser appUser;
    private BehaviourIfPoolWithWrongAnswersIsFull behaviourIfPoolWithWrongAnswersIsFull; 
    private int maxNumberOfWrongAnswersPerSession;
    public LearningStrategyFactory() {
    }

	public LearningStrategyFactory setName(String name) {
		this.name=name;
		return this;
	}

	public LearningStrategyFactory setId(Long id) {
		this.id = id;
		return this;
	}
	public LearningStrategyFactory setAppUser(AppUser appUser) {
		this.appUser=appUser;
		return this;
	}
   public LearningStrategyFactory setBehaviourIfPoolWithWrongAnswersIsFull(BehaviourIfPoolWithWrongAnswersIsFull behaviourIfPoolWithWrongAnswersIsFull) {
		this.behaviourIfPoolWithWrongAnswersIsFull = behaviourIfPoolWithWrongAnswersIsFull;
		return this;
	}

public LearningStrategyFactory setMaxNumberOfWrongAnswersPerSession(int maxNumberOfWrongAnswersPerSession) {
	this.maxNumberOfWrongAnswersPerSession = maxNumberOfWrongAnswersPerSession;
	return this;
}

public LearningStrategy getNewObject() {
    	return new LearningStrategy(//
    			this.id//
    			, this.name//
    			, this.maxNumberOfWrongAnswersPerSession//
    			, this.behaviourIfPoolWithWrongAnswersIsFull//
    			, this.appUser//
    	);
 	}
}
