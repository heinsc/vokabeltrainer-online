package de.heins.vokabeltrainerbackup;

import java.util.Optional;

public abstract class VokabeltrainerBackupTableHandler<SourceEntity, TargetEntity> {
	
	public abstract TargetEntity transferAndSaveSingleEntry(SourceEntity entity);
	
	public void handleTable() {
		Optional<Long> maxIdOptional = this.getMaxId();
		if (maxIdOptional.isPresent()) {
			Long maxId = maxIdOptional.get();
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
	}

	protected abstract Optional<SourceEntity> findById(long currentId);

	protected abstract Optional<Long> getMaxId();

}
