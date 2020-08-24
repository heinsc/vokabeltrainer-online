package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

import java.util.List;

import de.heins.vokabeltraineronline.web.entities.attributereference.SuccessStepAttrRef;

public class EditOrCreateSuccessStepModAttr {
	private SuccessStepAttrRef successStep;
	private List<String> selectableBehaviours;
	
	public SuccessStepAttrRef getSuccessStep() {
		return successStep;
	}
	public void setSuccessStep(SuccessStepAttrRef successStep) {
		this.successStep = successStep;
	}
	public List<String> getSelectableBehaviours() {
		return selectableBehaviours;
	}
	public void setSelectableBehaviours(List<String> selectableBehaviours) {
		this.selectableBehaviours = selectableBehaviours;
	}


}
