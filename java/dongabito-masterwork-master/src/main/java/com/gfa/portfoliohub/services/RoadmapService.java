package com.gfa.portfoliohub.services;

import com.gfa.portfoliohub.models.dtos.response.RoadmapDTO;
import com.gfa.portfoliohub.models.dtos.response.RoadmapResponse;
import com.gfa.portfoliohub.models.entities.Roadmap;
import org.springframework.security.core.Authentication;

public interface RoadmapService {
  RoadmapResponse getAllTheMaps();

  Roadmap findById(Integer id) throws IllegalArgumentException;

  void deleteRoadmap(Integer id, Authentication auth) throws IllegalArgumentException;

  RoadmapDTO saveRoadmap(Roadmap roadmap, Authentication auth) throws IllegalArgumentException;

  RoadmapDTO addSkillToRoadmap(Integer id, Integer skillId, Authentication auth)
      throws IllegalArgumentException;

  RoadmapDTO removeSkillToRoadmap(Integer id, Integer skillId, Authentication auth)
      throws IllegalArgumentException;

  RoadmapResponse findMapByOwner(Integer id) throws IllegalArgumentException;
}
