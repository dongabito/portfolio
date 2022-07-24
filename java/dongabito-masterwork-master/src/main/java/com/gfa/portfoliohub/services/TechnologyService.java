package com.gfa.portfoliohub.services;

import com.gfa.portfoliohub.models.dtos.response.TechnologyDTO;
import com.gfa.portfoliohub.models.dtos.response.TechnologyResponseDTO;
import com.gfa.portfoliohub.models.entities.Technology;
import java.util.List;

public interface TechnologyService {

  Technology getTechnologyById(Integer id) throws IllegalArgumentException;

  TechnologyResponseDTO findAllTheTechnologies();

  TechnologyDTO addTechnology(Technology technology);

  void deleteTechnology(Integer technologyId);

  List<String> listTheKeywords();

  TechnologyDTO addTechToSkill(Integer skillId, Integer id) throws IllegalArgumentException;

  void deleteTechFromSkill(Integer skillId, Integer id) throws IllegalArgumentException;
}
