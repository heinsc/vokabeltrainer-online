package de.heins.vokabeltraineronline.web.entities.htmlmodelattribute;

import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;

public class EditOrCreateIndexBoxModAtt {
	private IndexBoxAttrRef indexBox;
	private boolean mandatoryViolated;
	private boolean indexBoxWithThisNameAndSubjectAlreadyExists;
	public IndexBoxAttrRef getIndexBox() {
		return indexBox;
	}
	public void setIndexBox(IndexBoxAttrRef anIndexBox) {
		this.indexBox = anIndexBox;
	}
	public boolean isMandatoryViolated() {
		return mandatoryViolated;
	}
	public void setMandatoryViolated(boolean mandatoryViolated) {
		this.mandatoryViolated = mandatoryViolated;
	}
	public boolean isIndexBoxWithThisNameAndSubjectAlreadyExists() {
		return indexBoxWithThisNameAndSubjectAlreadyExists;
	}
	public void setIndexBoxWithThisNameAndSubjectAlreadyExists(boolean indexBoxWithThisNameAndSubjectAlreadyExists) {
		this.indexBoxWithThisNameAndSubjectAlreadyExists = indexBoxWithThisNameAndSubjectAlreadyExists;
	}


}
