package com.gfa.portfoliohub.repositories;

import com.gfa.portfoliohub.models.entities.Roadmap;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface RoadmapRepository extends CrudRepository<Roadmap, Integer> {
  List<Roadmap> findByProgrammerId(Integer id);

}
