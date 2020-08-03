package de.heins.vokabeltraineronline.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltraineronline.business.entity.IndexBox;
import de.heins.vokabeltraineronline.business.entity.User;

@Repository
public interface IndexBoxRepository extends CrudRepository<IndexBox, Long>{

	public List<IndexBox> findByUser(User user);

}
