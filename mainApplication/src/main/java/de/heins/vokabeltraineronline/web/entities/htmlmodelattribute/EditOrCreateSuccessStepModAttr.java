package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

import java.util.List;

import de.heins.vokabeltraineronline.web.entities.attributereference.SuccessStepAttrRef;

public class EditOrCreateSuccessStepModAttr {
	private boolean mandatoryViolated;
	private boolean successStepWithThisNameAlreadyExists;
	private SuccessStepAttrRef successStep;
	private List<String> selectableBehaviours;
	
	public boolean isMandatoryViolated() {
		return mandatoryViolated;
	}
	public void setMandatoryViolated(boolean mandatoryViolated) {
		this.mandatoryViolated = mandatoryViolated;
	}
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
	public boolean isSuccessStepWithThisNameAlreadyExists() {
		return successStepWithThisNameAlreadyExists;
	}
	public void setSuccessStepWithThisNameAlreadyExists(boolean successStepWithThisNameAlreadyExists) {
		this.successStepWithThisNameAlreadyExists = successStepWithThisNameAlreadyExists;
	}


}
