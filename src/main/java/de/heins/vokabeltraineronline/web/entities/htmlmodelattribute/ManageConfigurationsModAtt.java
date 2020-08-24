package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

import java.util.List;

import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;
import de.heins.vokabeltraineronline.web.entities.attributereference.LearningStrategyModAttr;
import de.heins.vokabeltraineronline.web.entities.attributereference.SuccessStepAttrRef;

public class ManageConfigurationsModAtt {
	private List<IndexBoxAttrRef> allIndexBoxes;
	private List<LearningStrategyModAttr> allLearningStrategies;
	private List<SuccessStepAttrRef> allSuccessSteps;

	public List<IndexBoxAttrRef> getAllIndexBoxes() {
		return allIndexBoxes;
	}

	public void setAllIndexBoxes(List<IndexBoxAttrRef> allIndexBoxes) {
		this.allIndexBoxes = allIndexBoxes;
	}

	public List<LearningStrategyModAttr> getAllLearningStrategies() {
		return allLearningStrategies;
	}

	public void setAllLearningStrategies(List<LearningStrategyModAttr> allLearningStrategies) {
		this.allLearningStrategies = allLearningStrategies;
	}

	public List<SuccessStepAttrRef> getAllSuccessSteps() {
		return allSuccessSteps;
	}

	public void setAllSuccessSteps(List<SuccessStepAttrRef> allSuccessSteps) {
		this.allSuccessSteps = allSuccessSteps;
	}
}
