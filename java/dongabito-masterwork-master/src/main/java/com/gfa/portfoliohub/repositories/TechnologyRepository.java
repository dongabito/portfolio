package com.gfa.portfoliohub.repositories;

import com.gfa.portfoliohub.models.entities.Technology;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TechnologyRepository extends CrudRepository<Technology, Integer> {

  @Query(value = "SELECT keyword FROM technologies t JOIN keywords k on t.id = k.technology_id", nativeQuery = true)
  List<String> listAllTheKeywords();
}
