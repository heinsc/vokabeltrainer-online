package de.heins.vokabeltraineronline.web.entities.attributereference;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.util.Strings;


public class LearningStrategyAttrRef {
	public LearningStrategyAttrRef() {
		super();
		this.assignedSuccessSteps = new LinkedList<String>();
	}

	private String name;
	private List<String> assignedSuccessSteps;
	public String getAssignedStepsCommaSeparated() {
		return Strings.join(assignedSuccessSteps, ',');
	};
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getAssignedSuccessSteps() {
		return assignedSuccessSteps;
	}

}
