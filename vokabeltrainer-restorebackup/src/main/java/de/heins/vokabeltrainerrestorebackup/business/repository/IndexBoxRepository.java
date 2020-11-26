package de.heins.vokabeltrainerrestorebackup.business.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerrestorebackup.business.entity.AppUser;
import de.heins.vokabeltrainerrestorebackup.business.entity.IndexBox;

@Repository
public interface IndexBoxRepository extends CrudRepository<IndexBox, Long>{
	public List<IndexBox> findByAppUserAndNameAndSubject(//
			AppUser appUser//
			, String name//
			, String subject//
	);
	
}
