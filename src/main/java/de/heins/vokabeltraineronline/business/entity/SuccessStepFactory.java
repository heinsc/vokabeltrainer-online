package de.heins.vokabeltraineronline.business.entity;

import org.springframework.stereotype.Component;

@Component
public class SuccessStepFactory {
    private Long id;

    private String name;
    
	private int durationOfNextAppearance;
	
	private LearningStrategy learningStrategy;
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
		public SuccessStepFactory setDurationOfNextAppearance(int durationOfNextAppearance) {
		this.durationOfNextAppearance = durationOfNextAppearance;
		return this;
	}
	public SuccessStepFactory setLearningStrategy(LearningStrategy learningStrategy) {
		this.learningStrategy = learningStrategy;
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
    			, this.durationOfNextAppearance//
    			, this.learningStrategy//
    			, this.user//
    	);
	}


}
