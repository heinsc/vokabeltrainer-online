package de.heins.vokabeltraineronline.web.entities.attributereference;

public class LearnProfileAttrRef {
	private int maxNumberOfWrongAnswersPerSession;
	private String behaviourIfPoolWithWrongAnswersIsFull;
	public int getMaxNumberOfWrongAnswersPerSession() {
		return maxNumberOfWrongAnswersPerSession;
	}
	public void setMaxNumberOfWrongAnswersPerSession(int maxNumberOfWrongAnswersPerSession) {
		this.maxNumberOfWrongAnswersPerSession = maxNumberOfWrongAnswersPerSession;
	}
	public String getBehaviourIfPoolWithWrongAnswersIsFull() {
		return behaviourIfPoolWithWrongAnswersIsFull;
	}
	public void setBehaviourIfPoolWithWrongAnswersIsFull(String behaviourIfPoolWithWrongAnswersIsFull) {
		this.behaviourIfPoolWithWrongAnswersIsFull = behaviourIfPoolWithWrongAnswersIsFull;
	}
	

}
