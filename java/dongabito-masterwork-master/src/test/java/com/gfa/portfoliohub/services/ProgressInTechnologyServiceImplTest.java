package com.gfa.portfoliohub.services;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gfa.portfoliohub.models.dtos.request.ProgressRequestDTO;
import com.gfa.portfoliohub.models.dtos.response.RoadmapDTO;
import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.models.entities.ProgressInTechnology;
import com.gfa.portfoliohub.models.entities.Roadmap;
import com.gfa.portfoliohub.models.entities.Technology;
import com.gfa.portfoliohub.models.entities.UserRole;
import com.gfa.portfoliohub.repositories.ProgressInTechnologyRepository;
import com.gfa.portfoliohub.util.TestEntities;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@DisplayName("Method ")
@ExtendWith(MockitoExtension.class)
class ProgressInTechnologyServiceImplTest {

  @Mock
  ProgressInTechnologyRepository repository;
  @Mock
  TechnologyService technologyService;
  @Mock
  RoadmapService roadmapService;
  @InjectMocks
  ProgressInTechnologyServiceImpl service;

  Authentication auth;
  Programmer programmer;
  ProgressRequestDTO requestDTO;
  ProgressInTechnology progress;
  Roadmap roadmap;
  RoadmapDTO roadmapDTO;
  Technology technology;

  @BeforeEach
  void setUp() {
    ModelMapper mapper = new ModelMapper();
    programmer = TestEntities.defaultProgrammer();
    auth = new UsernamePasswordAuthenticationToken(programmer, null,
        Arrays.asList(UserRole.ROLE_USER.getAuthority()));
    roadmap = programmer.getRoadmaps().get(0);
    technology = programmer.getRoadmaps().get(0).getSkills().get(0).getTechnologies().get(0);
    requestDTO = ProgressRequestDTO.builder()
        .learningProgress("BEGINNER")
        .details("bla-bla")
        .startedAt(Instant.now())
        .technology_id(technology.getId())
        .roadmap_id(roadmap.getId())
        .build();
    progress = TestEntities.defaultProgress();
    progress.setTechnology(technology);
  }

  @DisplayName("should create learningProgress for the given user")
  @Test
  void testIfMethodMakesProgress() {
    when(roadmapService.findById(anyInt())).thenReturn(roadmap);
    when(technologyService.getTechnologyById(anyInt())).thenReturn(technology);
    when(repository.save(any())).thenReturn(progress);
    RoadmapDTO roadmapDTOUnderTest = service.makeProgress(auth, requestDTO);
    assertTrue(programmer.getRoadmaps().get(0).getProgresses().contains(progress));
  }

  @DisplayName("should throws IAE for the given user")
  @Test
  void testIfMethodThrowsIAE() {
    when(roadmapService.findById(anyInt())).thenReturn(roadmap);
    when(technologyService.getTechnologyById(anyInt())).thenReturn(new Technology());
    assertThatIllegalArgumentException().isThrownBy(() -> service.makeProgress(auth, requestDTO))
        .withMessage("User has not such a roadmap or/and a technology");
  }

  @DisplayName("should update learningProgress for the given user")
  @Test
  void testIfMethodUpdatesProgress() {
    when(roadmapService.findById(anyInt())).thenReturn(roadmap);
    when(technologyService.getTechnologyById(anyInt())).thenReturn(technology);
    when(repository.findById(anyInt())).thenReturn(Optional.of(progress));
    when(repository.save(any())).thenReturn(progress);
    progress.setRoadmap(roadmap);
    RoadmapDTO roadmapDTOUnderTest = service.updateProgress(auth, requestDTO, progress.getId());
    assertTrue(programmer.getRoadmaps().get(0).getProgresses().contains(progress));
  }

  @DisplayName("should throws IAE for the given user")
  @Test
  void testIfMethodThrowsIAEByUpdate() {
    when(roadmapService.findById(anyInt())).thenReturn(roadmap);
    when(technologyService.getTechnologyById(anyInt())).thenReturn(new Technology());
    assertThatIllegalArgumentException().isThrownBy(
            () -> service.updateProgress(auth, requestDTO, 10000))
        .withMessage("User has not such a roadmap or/and a technology");
  }

  @DisplayName("should throws IAE for the given Technology Id")
  @Test
  void testIfMethodThrowsIAEByUpdateWithWrongTechId() {
    when(roadmapService.findById(anyInt())).thenReturn(roadmap);
    when(technologyService.getTechnologyById(anyInt())).thenReturn(technology);
    when(repository.findById(anyInt())).thenThrow(IllegalArgumentException.class);
    assertThatIllegalArgumentException().isThrownBy(
        () -> service.updateProgress(auth, requestDTO, 10000));
  }

  @DisplayName("should delete learningProgress for the given user")
  @Test
  void testIfMethodDeletesProgress() {
    when(repository.findById(anyInt())).thenReturn(Optional.of(progress));
    progress.setRoadmap(roadmap);
    service.deleteProgress(auth, progress.getId());
    verify(repository).deleteById(argThat(id -> id.equals(progress.getId())));
  }

  @DisplayName("should throws IAE by deleting learningProgress for the given user")
  @Test
  void testIfMethodShouldThrowIAEByDeletingProgress() {
    when(repository.findById(anyInt())).thenReturn(Optional.of(progress));
    verify(repository, never()).deleteById(anyInt());
    assertThatIllegalArgumentException().isThrownBy(() -> service.deleteProgress(auth, 10000))
        .withMessage("User has not such a progress");
  }

  @DisplayName("should check if progress is valid")
  @Test
  void testIfProgressIsValid() {
    assertTrue(service.checkIfValidProgress(programmer, roadmap, technology));
  }

  @DisplayName("should check if progress is invalid")
  @Test
  void testIfProgressIsInvalid() {
    assertFalse(service.checkIfValidProgress(programmer, roadmap, new Technology()));
  }

}