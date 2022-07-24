package com.gfa.portfoliohub.services;

import com.gfa.portfoliohub.models.dtos.request.ProgressRequestDTO;
import com.gfa.portfoliohub.models.dtos.response.RoadmapDTO;
import org.springframework.security.core.Authentication;

public interface ProgressInTechnologyService {

  RoadmapDTO makeProgress(Authentication authentication, ProgressRequestDTO progress)
      throws IllegalArgumentException;

  RoadmapDTO updateProgress(Authentication authentication, ProgressRequestDTO progress, Integer id)
      throws IllegalArgumentException;

  void deleteProgress(Authentication authentication, Integer id) throws IllegalArgumentException;
}
