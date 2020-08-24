package de.heins.vokabeltraineronline.web.entities.attributereference;


public class SuccessStepAttrRef {
	private String name;
	private int nextAppearanceInDays;
	
	private String behaviourIfWrong;
	//transient
	private boolean selected;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean b) {
		this.selected = b;
	}
	
	

}
