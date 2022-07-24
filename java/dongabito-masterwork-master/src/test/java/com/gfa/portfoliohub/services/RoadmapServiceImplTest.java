package com.gfa.portfoliohub.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gfa.portfoliohub.models.dtos.response.RoadmapDTO;
import com.gfa.portfoliohub.models.dtos.response.RoadmapResponse;
import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.models.entities.Roadmap;
import com.gfa.portfoliohub.models.entities.Skill;
import com.gfa.portfoliohub.models.entities.UserRole;
import com.gfa.portfoliohub.repositories.RoadmapRepository;
import com.gfa.portfoliohub.util.TestEntities;
import java.util.Arrays;
import java.util.Objects;
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
class RoadmapServiceImplTest {

  @Mock
  RoadmapRepository repository;
  @Mock
  SkillService skillService;
  @Mock
  ModelMapper mapper;
  @InjectMocks
  RoadmapServiceImpl service;

  Roadmap roadmap;
  RoadmapDTO roadmapDTO;
  Programmer programmer;
  Authentication auth;

  @BeforeEach
  void setUp() {
    programmer = TestEntities.defaultProgrammer();
    roadmap = programmer.getRoadmaps().get(0);
    roadmapDTO = new RoadmapDTO(roadmap);
  }

  @DisplayName("should return list of roadmaps")
  @Test
  void testIfMethodReturnsListOfRoadmaps() {
    when(repository.findAll()).thenReturn(Arrays.asList(roadmap));
    when(mapper.map(roadmap, RoadmapDTO.class)).thenReturn(roadmapDTO);
    assertThat(service.getAllTheMaps()).isInstanceOf(RoadmapResponse.class)
        .extracting(RoadmapResponse::getRoadmaps)
        .satisfiesAnyOf(roadmapDTOS -> Objects.equals(roadmapDTOS.get(0).getId(),
            roadmapDTO.getId()));
  }

  @DisplayName("should return the given roadmap")
  @Test
  void testIfMethodReturnsTheRoadmap() {
    when(repository.findById(anyInt())).thenReturn(Optional.of(roadmap));
    assertThat(service.findById(anyInt())).isInstanceOf(Roadmap.class)
        .extracting(Roadmap::getId).isEqualTo(roadmap.getId());
  }

  @DisplayName("should throw IAE when roadmap does not exist")
  @Test
  void testIfMethodThrowsIAEByNotExistingRoadmapId() {
    when(repository.findById(anyInt())).thenThrow(IllegalArgumentException.class);
    assertThatIllegalArgumentException().isThrownBy(() -> service.findById(anyInt()));
  }

  @DisplayName("should delete roadmap by user")
  @Test
  void testIfMethodDeleteTheGivenRoadmap() {
    auth = new UsernamePasswordAuthenticationToken(programmer, null,
        Arrays.asList(UserRole.ROLE_USER.getAuthority()));
    when(repository.findById(anyInt())).thenReturn(Optional.of(roadmap));
    service.deleteRoadmap(1, auth);
    verify(repository).delete(argThat(r -> Objects.equals(r, roadmap)));
    assertTrue(roadmap.getSkills().isEmpty());
  }

  @DisplayName("should delete roadmap by admin")
  @Test
  void testIfMethodDeleteTheGivenRoadmapByAdmin() {
    auth = new UsernamePasswordAuthenticationToken(TestEntities.defaultProgrammer(), null,
        Arrays.asList(UserRole.ROLE_ADMIN.getAuthority()));
    when(repository.findById(anyInt())).thenReturn(Optional.of(roadmap));
    service.deleteRoadmap(1, auth);
    verify(repository).delete(argThat(r -> Objects.equals(r, roadmap)));
    assertTrue(roadmap.getSkills().isEmpty());
  }

  @DisplayName("should throw IAE by deleting a roadmap by other user")
  @Test
  void testIfMethodThrowsIAEByDeleting() {
    auth = new UsernamePasswordAuthenticationToken(TestEntities.defaultProgrammer(), null,
        Arrays.asList(UserRole.ROLE_USER.getAuthority()));
    when(repository.findById(anyInt())).thenReturn(Optional.of(roadmap));
    verify(repository, never()).delete(any());
    assertThatIllegalArgumentException().isThrownBy(() -> service.deleteRoadmap(1, auth))
        .withMessage("This method is not allowed");
  }

  @DisplayName("should save a roadmap")
  @Test
  void testIfMethodSaveTheGivenRoadmap() {
    auth = new UsernamePasswordAuthenticationToken(programmer, null,
        Arrays.asList(UserRole.ROLE_USER.getAuthority()));
    Roadmap roadmapToSave = TestEntities.defaultRoadmap();
    when(repository.save(roadmapToSave)).thenReturn(roadmapToSave);
    assertThat(service.saveRoadmap(roadmapToSave, auth)).isInstanceOf(RoadmapDTO.class)
        .extracting(r -> r.getProgrammer().getFirstName()).isEqualTo(programmer.getFirstName());
  }

  @DisplayName("should throw IAE by saving a roadmap with a not existing ID")
  @Test
  void testIfMethodThrowsIAEBySavingWithWrongID() {
    auth = new UsernamePasswordAuthenticationToken(programmer, null,
        Arrays.asList(UserRole.ROLE_USER.getAuthority()));
    when(repository.findById(anyInt())).thenThrow(IllegalArgumentException.class);
    assertThatIllegalArgumentException().isThrownBy(() -> service.saveRoadmap(roadmap, auth));
  }

  @DisplayName("should update a roadmap")
  @Test
  void testIfMethodUpdateTheGivenRoadmap() {
    auth = new UsernamePasswordAuthenticationToken(programmer, null,
        Arrays.asList(UserRole.ROLE_USER.getAuthority()));
    when(repository.findById(anyInt())).thenReturn(Optional.of(roadmap));
    roadmap.setProgrammer(programmer);
    assertThat(service.saveRoadmap(roadmap, auth)).isInstanceOf(RoadmapDTO.class)
        .extracting(r -> r.getProgrammer().getFirstName()).isEqualTo(programmer.getFirstName());
  }

  @DisplayName("should add skill to given roadmap")
  @Test
  void testIfMethodAddSkillToRoadmap() {
    auth = new UsernamePasswordAuthenticationToken(programmer, null,
        Arrays.asList(UserRole.ROLE_USER.getAuthority()));
    Skill skill = programmer.getSkills().get(0);
    when(skillService.findById(anyInt())).thenReturn(skill);
    when(repository.findById(anyInt())).thenReturn(Optional.of(roadmap));
    when(repository.save(roadmap)).thenReturn(roadmap);
    assertThat(service.addSkillToRoadmap(1, 1, auth)).isInstanceOf(RoadmapDTO.class)
        .extracting(r -> r.getSkills().get(0).getName()).isEqualTo(skill.getName());
  }

  @DisplayName("should add skill to others roadmap by admin")
  @Test
  void testIfMethodAddSkillToOthersRoadmapByAdmin() {
    auth = new UsernamePasswordAuthenticationToken(programmer, null,
        Arrays.asList(UserRole.ROLE_ADMIN.getAuthority()));
    Skill skill = programmer.getSkills().get(0);
    when(skillService.findById(anyInt())).thenReturn(skill);
    when(repository.findById(anyInt())).thenReturn(Optional.of(TestEntities.defaultRoadmap()));
    when(repository.save(any())).thenReturn(roadmap);
    assertThat(service.addSkillToRoadmap(1, 1, auth)).isInstanceOf(RoadmapDTO.class);
  }

  @DisplayName("should throw IAE by adding skill to others roadmap by user")
  @Test
  void testIfMethodThrowsIAEAddingSkillToOthersRoadmapByUser() {
    auth = new UsernamePasswordAuthenticationToken(programmer, null,
        Arrays.asList(UserRole.ROLE_USER.getAuthority()));
    Skill skill = programmer.getSkills().get(0);
    when(skillService.findById(anyInt())).thenReturn(skill);
    when(repository.findById(anyInt())).thenReturn(Optional.of(TestEntities.defaultRoadmap()));
    assertThatIllegalArgumentException().isThrownBy(() -> service.addSkillToRoadmap(1, 1, auth))
        .withMessage("Not appropriate input");
  }

  @DisplayName("should throw IAE by adding skill to roadmap with a not existing ID")
  @Test
  void testIfMethodThrowsIAEByAddingSkillToRoadmapWithWrongID() {
    auth = new UsernamePasswordAuthenticationToken(programmer, null,
        Arrays.asList(UserRole.ROLE_USER.getAuthority()));
    when(repository.findById(anyInt())).thenThrow(IllegalArgumentException.class);
    assertThatIllegalArgumentException().isThrownBy(() -> service.addSkillToRoadmap(1, 1, auth));
  }

  @DisplayName("should remove skill from given roadmap")
  @Test
  void testIfMethodRemoveSkillFromRoadmap() {
    auth = new UsernamePasswordAuthenticationToken(programmer, null,
        Arrays.asList(UserRole.ROLE_USER.getAuthority()));
    Skill skill = programmer.getSkills().get(0);
    when(repository.save(roadmap)).thenReturn(roadmap);
    assertThat(service.removeSkillToRoadmap(roadmap.getId(), skill.getId(), auth)).isInstanceOf(
        RoadmapDTO.class);
    assertFalse(roadmap.getSkills().contains(skill));
  }

  @DisplayName("should throw IAE by removing skill from roadmap with a not existing ID")
  @Test
  void testIfMethodThrowsIAEByRemovingSkillFromRoadmapWithWrongID() {
    auth = new UsernamePasswordAuthenticationToken(programmer, null,
        Arrays.asList(UserRole.ROLE_USER.getAuthority()));
    assertThatIllegalArgumentException().isThrownBy(() -> service.removeSkillToRoadmap(1, 1, auth));
  }

  @DisplayName("should return list of roadmaps searched by its Owner")
  @Test
  void testIfMethodReturnsListOfRoadmapsSearchedByOwner() {
    when(repository.findByProgrammerId(anyInt())).thenReturn(Arrays.asList(roadmap));
    when(mapper.map(any(), any())).thenReturn(roadmapDTO);
    assertThat(service.findMapByOwner(anyInt())).isInstanceOf(RoadmapResponse.class);
  }
}