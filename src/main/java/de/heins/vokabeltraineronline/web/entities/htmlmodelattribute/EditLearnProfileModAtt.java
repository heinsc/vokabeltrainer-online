package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

import java.util.List;

import de.heins.vokabeltraineronline.web.entities.attributereference.LearnProfileAttrRef;

public class EditLearnProfileModAtt {
	private LearnProfileAttrRef learnProfile;
	private List<String> selectableBehavioursIfPoolWithWrongAnswersIsFull;

	public LearnProfileAttrRef getLearnProfile() {
		return learnProfile;
	}

	public void setLearnProfile(LearnProfileAttrRef learnProfile) {
		this.learnProfile = learnProfile;
	}
	public void setSelectableBehavioursIfPoolWithWrongAnswersIsFull(
			List<String> selectableBehavioursIfPoolWithWrongAnswersIsFull) {
		this.selectableBehavioursIfPoolWithWrongAnswersIsFull = selectableBehavioursIfPoolWithWrongAnswersIsFull;
	}

	public List<String> getSelectableBehavioursIfPoolWithWrongAnswersIsFull() {
		return selectableBehavioursIfPoolWithWrongAnswersIsFull;
	}
}
