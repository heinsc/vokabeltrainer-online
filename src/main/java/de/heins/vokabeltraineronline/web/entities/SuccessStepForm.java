package de.heins.vokabeltraineronline.web.entities;


public class SuccessStepForm {
	private String name;
	private int nextAppearanceInDays;
	public int getNextAppearanceInDays() {
		return nextAppearanceInDays;
	}

	public void setNextAppearanceInDays(int nextAppearanceInDays) {
		this.nextAppearanceInDays = nextAppearanceInDays;
	}

	public String getBehaviourIfWrong() {
		return behaviourIfWrong;
	}

	public void setBehaviourIfWrong(String behaviourIfWrong) {
		this.behaviourIfWrong = behaviourIfWrong;
	}

	private String behaviourIfWrong;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
