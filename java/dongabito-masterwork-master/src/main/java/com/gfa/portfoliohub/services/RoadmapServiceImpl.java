package com.gfa.portfoliohub.services;

import com.gfa.portfoliohub.models.dtos.response.RoadmapDTO;
import com.gfa.portfoliohub.models.dtos.response.RoadmapResponse;
import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.models.entities.Roadmap;
import com.gfa.portfoliohub.models.entities.Skill;
import com.gfa.portfoliohub.models.entities.UserRole;
import com.gfa.portfoliohub.repositories.RoadmapRepository;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RoadmapServiceImpl implements RoadmapService {

  private RoadmapRepository repository;
  private SkillService skillService;
  private ModelMapper mapper;

  @Override
  public RoadmapResponse getAllTheMaps() {
    return new RoadmapResponse(StreamSupport.stream(repository.findAll().spliterator(), false)
        .map(r -> mapper.map(r, RoadmapDTO.class))
        .collect(Collectors.toList()));
  }

  @Override
  public Roadmap findById(Integer id) throws IllegalArgumentException {
    return repository.findById(id).orElseThrow(IllegalAccessError::new);
  }

  @Override
  public void deleteRoadmap(Integer id, Authentication auth) throws IllegalArgumentException {
    Programmer programmer = (Programmer) auth.getPrincipal();
    boolean isAdmin = auth.getAuthorities().contains(UserRole.ROLE_ADMIN.getAuthority());
    Roadmap roadmapToDelete = repository.findById(id).orElseThrow(IllegalArgumentException::new);
    if (isAdmin || programmer.getRoadmaps().contains(roadmapToDelete)) {
      roadmapToDelete.setProgrammer(null);
      roadmapToDelete.setSkills(new ArrayList<>());
      repository.delete(roadmapToDelete);
    } else {
      throw new IllegalArgumentException("This method is not allowed");
    }
  }

  @Override
  @Transactional
  public RoadmapDTO saveRoadmap(Roadmap roadmap, Authentication authentication)
      throws IllegalArgumentException {
    Programmer programmer = (Programmer) authentication.getPrincipal();
    if (programmer.getRoadmaps().contains(roadmap)) {
      Roadmap roadmapToUpdate =
          repository.findById(roadmap.getId()).orElseThrow(IllegalArgumentException::new);
      roadmapToUpdate.setName(roadmap.getName());
      roadmapToUpdate.setDetails(roadmap.getDetails());
      return new RoadmapDTO(roadmapToUpdate);
    }
    roadmap.setProgrammer(programmer);
    return new RoadmapDTO(repository.save(roadmap));
  }

  @Override
  @Transactional
  public RoadmapDTO addSkillToRoadmap(Integer id, Integer skillId, Authentication auth)
      throws IllegalArgumentException {
    Skill skill = skillService.findById(skillId);
    Programmer programmer = (Programmer) auth.getPrincipal();
    boolean isAdmin = auth.getAuthorities().contains(UserRole.ROLE_ADMIN.getAuthority());
    Roadmap roadmap = repository.findById(id).orElseThrow(IllegalArgumentException::new);
    if (programmer.getRoadmaps().contains(roadmap) || isAdmin) {
      roadmap.getSkills().add(skill);
      return new RoadmapDTO(repository.save(roadmap));
    } else {
      throw new IllegalArgumentException("Not appropriate input");
    }
  }

  @Override
  public RoadmapDTO removeSkillToRoadmap(Integer id, Integer skillId, Authentication auth)
      throws IllegalArgumentException {
    Programmer programmer = (Programmer) auth.getPrincipal();
    Roadmap roadmap = programmer.getRoadmaps().stream()
        .filter(r -> Objects.equals(r.getId(), id))
        .findFirst().orElseThrow(IllegalArgumentException::new);
    roadmap.getSkills().removeIf(s -> Objects.equals(s.getId(), skillId));
    return new RoadmapDTO(repository.save(roadmap));
  }

  @Override
  public RoadmapResponse findMapByOwner(Integer id) throws IllegalArgumentException {
    return new RoadmapResponse(
        repository.findByProgrammerId(id).stream().map(r -> mapper.map(r, RoadmapDTO.class))
            .collect(Collectors.toList()));
  }
}
