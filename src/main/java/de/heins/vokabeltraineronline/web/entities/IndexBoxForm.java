package de.heins.vokabeltraineronline.web.entities;

public class IndexBoxForm {
	private String name;
	private LearningStrategyForm learningStrategyForm;

	public LearningStrategyForm getLearningStrategyForm() {
		return learningStrategyForm;
	}

	public void setLearningStrategyForm(LearningStrategyForm learningStrategyForm) {
		this.learningStrategyForm = learningStrategyForm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
