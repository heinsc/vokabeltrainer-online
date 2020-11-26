package de.heins.vokabeltrainerrestorebackup;

import java.util.List;
import java.util.Optional;
 
public abstract class VokabeltrainerRestoreBackupTableHandler<SourceEntity, TargetEntity> {
	
	public abstract TargetEntity transferAndSaveSingleEntry(SourceEntity entity);
	
	public void handleTable() {
		List<Long> maxIdList = this.getMaxId();
		Long maxId = maxIdList.get(0);
		for (long currentId = 0; currentId < maxId; currentId++) {
			Optional<SourceEntity> foundByIdResultList = this.findById(currentId);
			if (foundByIdResultList.isPresent()) {
				TargetEntity targetEntity = transferAndSaveSingleEntry(//
						foundByIdResultList.get()//
				);
				System.out.println(targetEntity.getClass() + ": Done " + currentId + "/" + maxId);
			}
		}
	}

	protected abstract Optional<SourceEntity> findById(long currentId);

	protected abstract List<Long> getMaxId();

}
