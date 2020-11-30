package de.heins.vokabeltraineronline.web.entities.attributereference;


public class SuccessStepAttrRef {
	private String name;
	private int nextAppearanceInDays;
	private String faultTolerance;
	
	private String behaviourIfWrong;
	public int getNextAppearanceInDays() {
		return nextAppearanceInDays;
	}

	public void setNextAppearanceInDays(int nextAppearanceInDays) {
		this.nextAppearanceInDays = nextAppearanceInDays;
	}

	public String getFaultTolerance() {
		return faultTolerance;
	}

	public void setFaultTolerance(String faultTolerance) {
		this.faultTolerance = faultTolerance;
	}

	public String getBehaviourIfWrong() {
		return behaviourIfWrong;
	}

	public void setBehaviourIfWrong(String behaviourIfWrong) {
		this.behaviourIfWrong = behaviourIfWrong;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
