package de.heins.vokabeltraineronline.web.entities.attributereference;

import java.util.HashSet;
import java.util.Set;

public class IndexBoxAttrRef {
	public IndexBoxAttrRef() {
		super();
		this.questionsWithAnswers = new HashSet<QuestionWithAnswerAttrRef>();
	}

	private String name;
	private String subject;
	private Set<QuestionWithAnswerAttrRef> questionsWithAnswers;
	private boolean filterOn;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public boolean isFilterOn() {
		return filterOn;
	}

	public void setFilterOn(boolean filterOn) {
		this.filterOn = filterOn;
	}

	public Set<QuestionWithAnswerAttrRef> getQuestionsWithAnswers() {
		return questionsWithAnswers;
	}
	

}
