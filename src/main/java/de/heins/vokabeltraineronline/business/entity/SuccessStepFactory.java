package de.heins.vokabeltraineronline.business.entity;

import org.springframework.stereotype.Component;

@Component
public class SuccessStepFactory {
    private Long id;

    private String name;
    
	private int nextAppearanceInDays;
	
	private LearningStrategy learningStrategy;
	private BehaviourIfWrong behaviourIfWrong;
	private User user;
    public SuccessStepFactory() {
    }

	public SuccessStepFactory setId(Long id) {
		this.id = id;
		return this;
	}
	public SuccessStepFactory setName(String name) {
		this.name = name;
		return this;
	}
		public SuccessStepFactory setNextAppearanceInDays(int durationOfNextAppearance) {
		this.nextAppearanceInDays = durationOfNextAppearance;
		return this;
	}
	public SuccessStepFactory setLearningStrategy(LearningStrategy learningStrategy) {
		this.learningStrategy = learningStrategy;
		return this;
	}
	public SuccessStepFactory setBehaviourIfWrong(BehaviourIfWrong behaviourIfWrong) {
		this.behaviourIfWrong = behaviourIfWrong;
		return this;
	}

	public SuccessStepFactory setUser(User user) {
		this.user=user;
		return this;
	}
    public SuccessStep getNewObject() {
    	return new SuccessStep(//
    			this.id//
    			, this.name//
    			, this.nextAppearanceInDays//
    			, this.learningStrategy//
    			, this.behaviourIfWrong//
    			, this.user//
    	);
	}


}
