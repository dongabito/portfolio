package com.gfa.portfoliohub.services;

import com.gfa.portfoliohub.models.dtos.request.ProgressRequestDTO;
import com.gfa.portfoliohub.models.dtos.response.RoadmapDTO;
import com.gfa.portfoliohub.models.entities.LearningProgress;
import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.models.entities.ProgressInTechnology;
import com.gfa.portfoliohub.models.entities.Roadmap;
import com.gfa.portfoliohub.models.entities.Technology;
import com.gfa.portfoliohub.repositories.ProgressInTechnologyRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProgressInTechnologyServiceImpl
    implements ProgressInTechnologyService {

  private ProgressInTechnologyRepository repository;
  private TechnologyService technologyService;
  private RoadmapService roadmapService;

  @Override
  public RoadmapDTO makeProgress(Authentication authentication,
                                 ProgressRequestDTO progress)
      throws IllegalArgumentException {
    Programmer programmer = (Programmer) authentication.getPrincipal();
    Roadmap roadmap = roadmapService.findById(progress.getRoadmap_id());
    Technology technology = technologyService.getTechnologyById(progress.getTechnology_id());
    if (checkIfValidProgress(programmer, roadmap, technology)) {
      ProgressInTechnology progressToSave = repository.save(ProgressInTechnology.builder()
          .progress(LearningProgress.valueOf(progress.getLearningProgress()))
          .details(progress.getDetails())
          .startedAt(progress.getStartedAt())
          .technology(technology)
          .roadmap(roadmap)
          .build());
      progressToSave.setRoadmap(roadmap);
      return new RoadmapDTO(roadmap);
    } else {
      throw new IllegalArgumentException("User has not such a roadmap or/and a technology");
    }
  }

  @Override
  public RoadmapDTO updateProgress(Authentication authentication,
                                   ProgressRequestDTO progress, Integer id)
      throws IllegalArgumentException {
    Programmer programmer = (Programmer) authentication.getPrincipal();
    Roadmap roadmap = roadmapService.findById(progress.getRoadmap_id());
    Technology technology = technologyService.getTechnologyById(progress.getTechnology_id());
    if (checkIfValidProgress(programmer, roadmap, technology)) {
      ProgressInTechnology progressToUpdate =
          repository.findById(id).orElseThrow(IllegalArgumentException::new);
      progressToUpdate.setProgress(LearningProgress.valueOf(progress.getLearningProgress()));
      progressToUpdate.setDetails(progress.getDetails());
      progressToUpdate.setStartedAt(progress.getStartedAt());
      repository.save(progressToUpdate);
      return new RoadmapDTO(roadmap);
    } else {
      throw new IllegalArgumentException("User has not such a roadmap or/and a technology");
    }
  }

  @Override
  public void deleteProgress(Authentication authentication, Integer id)
      throws IllegalArgumentException {
    Programmer programmer = (Programmer) authentication.getPrincipal();
    ProgressInTechnology progress =
        repository.findById(id).orElseThrow(IllegalArgumentException::new);
    if (programmer.getRoadmaps().stream()
        .anyMatch(r -> r.getProgresses().contains(progress))) {
      progress.getRoadmap().getProgresses().remove(progress);
      progress.setRoadmap(new Roadmap());
      repository.deleteById(id);
    } else {
      throw new IllegalArgumentException("User has not such a progress");
    }
  }

  public boolean checkIfValidProgress(Programmer programmer, Roadmap roadmap,
                                      Technology technology) {
    return roadmap.getSkills().stream()
        .anyMatch(s -> s.getTechnologies().contains(technology)) &&
        programmer.getRoadmaps().contains(roadmap);
  }
}
