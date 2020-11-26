package de.heins.vokabeltrainerdropbackup.business.entity.backup;

public enum FaultToleranceBackup {
	NO_TOLERANCE //
	, EVERY_TENTH_CHARACTER_MAY_BE_FALSE //
	, EVERY_EIGHTH_CHARACTER_MAY_BE_FALSE //
	, EVERY_SIXTH_CHARACTER_MAY_BE_FALSE //
	, EVERY_FIFTH_CHARACTER_MAY_BE_FALSE //
	, ORALLY
}
