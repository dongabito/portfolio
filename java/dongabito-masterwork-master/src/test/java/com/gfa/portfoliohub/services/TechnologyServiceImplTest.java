package com.gfa.portfoliohub.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gfa.portfoliohub.models.dtos.response.TechnologyDTO;
import com.gfa.portfoliohub.models.dtos.response.TechnologyResponseDTO;
import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.models.entities.Skill;
import com.gfa.portfoliohub.models.entities.Technology;
import com.gfa.portfoliohub.repositories.TechnologyRepository;
import com.gfa.portfoliohub.util.TestEntities;
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

@DisplayName("Method ")
@ExtendWith(MockitoExtension.class)
class TechnologyServiceImplTest {

  @Mock
  TechnologyRepository repository;
  @Mock
  SkillService skillService;
  @Mock
  ModelMapper mapper;
  @InjectMocks
  TechnologyServiceImpl service;

  Programmer programmer;
  Skill skill;
  Technology technology;
  TechnologyDTO dto;

  @BeforeEach
  void setUp() {
    ModelMapper mapper = new ModelMapper();
    programmer = TestEntities.defaultProgrammer();
    skill = programmer.getSkills().get(0);
    technology = skill.getTechnologies().get(0);
    dto = mapper.map(technology, TechnologyDTO.class);
  }

  @DisplayName("should find the given technology by Id")
  @Test
  void testIfMethodGetTheGivenTechnologyById() {
    when(repository.findById(anyInt())).thenReturn(Optional.of(technology));
    assertThat(service.getTechnologyById(technology.getId())).isInstanceOf(Technology.class)
        .extracting(Technology::getId).isEqualTo(technology.getId());
  }

  @DisplayName("should throw IAE by getting the technology with non existing ID")
  @Test
  void testIfMethodThrowsIaeGettingTheTechnologyWithNonExistingId() {
    when(repository.findById(anyInt())).thenThrow(IllegalArgumentException.class);
    assertThatIllegalArgumentException().isThrownBy(() -> service.getTechnologyById(anyInt()));
  }

  @DisplayName("should return a list of all the technologies")
  @Test
  void testIfMethodShouldReturnAllTheTechnologies() {
    when(repository.findAll()).thenReturn(Arrays.asList(technology));
    assertThat(service.findAllTheTechnologies()).isInstanceOf(TechnologyResponseDTO.class);
  }

  @DisplayName("should add technology")
  @Test
  void testIfMethodSaveTheTechnology() {
    when(repository.save(any())).thenReturn(technology);
    when(mapper.map(any(), any())).thenReturn(dto);
    assertThat(service.addTechnology(technology)).isInstanceOf(TechnologyDTO.class)
        .extracting(TechnologyDTO::getId).isEqualTo(technology.getId());
  }

  @DisplayName("should delete the technology")
  @Test
  void testIfMethodDeleteTheTechnology() {
    when(repository.existsById(anyInt())).thenReturn(true);
    service.deleteTechnology(technology.getId());
    verify(repository).deleteById(argThat(s -> s.equals(technology.getId())));
  }

  @DisplayName("should throw IAE by deleting the technology with non existing ID")
  @Test
  void testIfMethodThrowsIaeByDeletingTheTechnologyWithNonExistingId() {
    when(repository.existsById(anyInt())).thenReturn(false);
    verify(repository, never()).deleteById(anyInt());
    assertThatIllegalArgumentException().isThrownBy(
            () -> service.deleteTechnology(technology.getId()))
        .withMessage("No such technology");
  }

  @DisplayName("should throw IAE by deleting the technology with null ID")
  @Test
  void testIfMethodThrowsIaeByDeletingTheTechnologyWithNullId() {
    when(repository.existsById(anyInt())).thenThrow(IllegalArgumentException.class);
    verify(repository, never()).deleteById(anyInt());
    assertThatIllegalArgumentException().isThrownBy(
        () -> service.deleteTechnology(technology.getId()));
  }

  @DisplayName("should return a list of all the keywords")
  @Test
  void testIfMethodShouldReturnAllTheSkills() {
    when(repository.listAllTheKeywords()).thenReturn(Arrays.asList("Java", "xml"));
    assertThat(service.listTheKeywords()).hasSize(2).anyMatch(s -> s.equals("Java"));
  }

  @DisplayName("should add technology to the given skill")
  @Test
  void testIfMethodAddTechnologyToTheGivenSkill() {
    when(repository.findById(anyInt())).thenReturn(Optional.of(technology));
    when(skillService.findById(anyInt())).thenReturn(skill);
    when(repository.save(any())).thenReturn(technology);
    when(mapper.map(any(), any())).thenReturn(dto);
    assertThat(service.addTechToSkill(skill.getId(), technology.getId())).isInstanceOf(
        TechnologyDTO.class);
  }

  @DisplayName("should throw IAE by adding the technology with null ID to skill")
  @Test
  void testIfMethodThrowsIaeByAddingTheTechnologyWithNullIdToSkill() {
    when(repository.existsById(anyInt())).thenThrow(IllegalArgumentException.class);
    verify(skillService, never()).findById(anyInt());
    verify(repository, never()).save(any());
    assertThatIllegalArgumentException().isThrownBy(
        () -> service.deleteTechnology(technology.getId()));
  }

  @DisplayName("should delete technology from the given skill")
  @Test
  void testIfMethodDeletesTechnologyFromTheGivenSkill() {
    when(repository.findById(anyInt())).thenReturn(Optional.of(technology));
    when(skillService.findById(anyInt())).thenReturn(skill);
    technology.getSkills().add(skill);
    service.deleteTechFromSkill(skill.getId(), technology.getId());
    verify(repository).save(argThat(t -> t.getId().equals(technology.getId())));
  }

  @DisplayName("should throw IAE by deleting the technology with null ID to skill")
  @Test
  void testIfMethodThrowsIaeByDeletingTheTechnologyWithNullIdToSkill() {
    when(repository.findById(anyInt())).thenThrow(IllegalArgumentException.class);
    verify(skillService, never()).findById(anyInt());
    verify(repository, never()).save(any());
    assertThatIllegalArgumentException().isThrownBy(
        () -> service.deleteTechFromSkill(technology.getId(), technology.getId()));
  }

}