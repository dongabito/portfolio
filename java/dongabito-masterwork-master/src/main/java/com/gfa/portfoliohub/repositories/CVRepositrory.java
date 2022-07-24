package com.gfa.portfoliohub.repositories;

import com.gfa.portfoliohub.models.entities.CurriculumVitae;
import org.springframework.data.repository.CrudRepository;

public interface CVRepositrory extends CrudRepository<CurriculumVitae, Integer> {
}
