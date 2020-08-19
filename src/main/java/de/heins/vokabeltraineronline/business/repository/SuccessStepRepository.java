package de.heins.vokabeltraineronline.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltraineronline.business.entity.SuccessStep;
import de.heins.vokabeltraineronline.business.entity.AppUser;

@Repository
public interface SuccessStepRepository extends CrudRepository<SuccessStep, Long>{
	public List<SuccessStep> findByAppUser(AppUser appUser);

	public List<SuccessStep> findByAppUserAndName(AppUser appUser, String name);

}
