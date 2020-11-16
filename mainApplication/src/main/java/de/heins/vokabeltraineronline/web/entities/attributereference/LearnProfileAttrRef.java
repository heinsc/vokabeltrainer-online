package de.heins.vokabeltraineronline.web.entities.attributereference;

public class LearnProfileAttrRef {
	private int maxNumberOfWrongAnswersPerSession;
	private String behaviourIfPoolWithWrongAnswersIsFull;
	private String faultTolerance;
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
	public String getFaultTolerance() {
		return faultTolerance;
	}
	public void setFaultTolerance(String faultTolerance) {
		this.faultTolerance = faultTolerance;
	}
	

}
