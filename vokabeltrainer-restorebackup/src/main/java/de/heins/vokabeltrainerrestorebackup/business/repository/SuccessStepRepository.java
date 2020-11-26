package de.heins.vokabeltrainerrestorebackup.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerrestorebackup.business.entity.AppUser;
import de.heins.vokabeltrainerrestorebackup.business.entity.SuccessStep;

@Repository
public interface SuccessStepRepository extends CrudRepository<SuccessStep, Long>{
	public List<SuccessStep> findByAppUserAndName(AppUser appUser, String name);

}
