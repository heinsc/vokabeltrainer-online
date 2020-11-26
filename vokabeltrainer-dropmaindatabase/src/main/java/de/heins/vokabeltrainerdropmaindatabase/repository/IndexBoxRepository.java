package de.heins.vokabeltrainerdropmaindatabase.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heins.vokabeltrainerdropmaindatabase.entity.IndexBox;

@Repository
public interface IndexBoxRepository extends CrudRepository<IndexBox, Long>{
}
