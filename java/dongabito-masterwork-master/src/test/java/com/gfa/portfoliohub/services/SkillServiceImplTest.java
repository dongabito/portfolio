package com.gfa.portfoliohub.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gfa.portfoliohub.models.dtos.response.SkillCategoryResponseDto;
import com.gfa.portfoliohub.models.dtos.response.SkillDTO;
import com.gfa.portfoliohub.models.dtos.response.SkillResponseDTO;
import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.models.entities.Skill;
import com.gfa.portfoliohub.repositories.SkillRepository;
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
class SkillServiceImplTest {

  @Mock
  SkillRepository repository;
  @Mock
  ModelMapper mapper;
  @InjectMocks
  SkillServiceImpl service;

  Programmer programmer;
  Skill skill;
  SkillDTO skillDTO;

  @BeforeEach
  void setUp() {
    programmer = TestEntities.defaultProgrammer();
    skill = programmer.getSkills().get(0);
    skillDTO = new SkillDTO(skill);
  }

  @DisplayName("should return a list of all the skills")
  @Test
  void testIfMethodShouldReturnAllTheSkills() {
    when(repository.findAll()).thenReturn(Arrays.asList(skill));
    assertThat(service.findAllTheSkills()).isInstanceOf(SkillResponseDTO.class);
  }

  @DisplayName("should return a list of all the skillCategory")
  @Test
  void testIfMethodShouldReturnAllTheSkillCategory() {
    assertThat(service.getAllTheSkillCategory()).isInstanceOf(SkillCategoryResponseDto.class);
    assertThat(service.getAllTheSkillCategory().getSkillCategories()).filteredOn(
        s -> s.name().equals("Java"));
  }

  @DisplayName("should save the skill")
  @Test
  void testIfMethodSaveTheSkill() {
    when(repository.save(any())).thenReturn(skill);
    when(mapper.map(any(), any())).thenReturn(skillDTO);
    Skill skillToSave = TestEntities.defaultSkill();
    skillToSave.setId(null);
    assertThat(service.save(skillToSave)).isInstanceOf(SkillDTO.class)
        .extracting(SkillDTO::getId).isEqualTo(skill.getId());
  }

  @DisplayName("should update the skill")
  @Test
  void testIfMethodUpdateTheSkill() {
    when(repository.findById(anyInt())).thenReturn(Optional.of(skill));
    when(repository.save(any())).thenReturn(skill);
    when(mapper.map(any(), any())).thenReturn(skillDTO);
    assertThat(service.save(skill)).isInstanceOf(SkillDTO.class)
        .extracting(SkillDTO::getId).isEqualTo(skill.getId());
  }

  @DisplayName("should throw IAE by updating the skill with non existing Id")
  @Test
  void testIfMethodThrowsIaeByUpdatingTheSkillWithNonExistingId() {
    when(repository.findById(anyInt())).thenThrow(IllegalArgumentException.class);
    assertThatIllegalArgumentException().isThrownBy(() -> service.save(skill));
  }

  @DisplayName("should delete the skill")
  @Test
  void testIfMethodDeleteTheSkill() {
    when(repository.existsById(anyInt())).thenReturn(true);
    service.deleteSkill(skill.getId());
    verify(repository).deleteById(argThat(s -> s.equals(skill.getId())));
  }

  @DisplayName("should throw IAE by deleting the skill with non existing ID")
  @Test
  void testIfMethodThrowsIaeByDeletingTheSkillWithNonExistingId() {
    when(repository.existsById(anyInt())).thenReturn(false);
    verify(repository, never()).deleteById(anyInt());
    assertThatIllegalArgumentException().isThrownBy(() -> service.deleteSkill(skill.getId()))
        .withMessage("No such skill was found");
  }

  @DisplayName("should throw IAE by deleting the skill with null ID")
  @Test
  void testIfMethodThrowsIaeByDeletingTheSkillWithNullId() {
    when(repository.existsById(anyInt())).thenThrow(IllegalArgumentException.class);
    verify(repository, never()).deleteById(anyInt());
    assertThatIllegalArgumentException().isThrownBy(() -> service.deleteSkill(skill.getId()));
  }

  @DisplayName("should find the given skill by Id")
  @Test
  void testIfMethodGetTheGivenSkillById() {
    when(repository.findById(anyInt())).thenReturn(Optional.of(skill));
    assertThat(service.findById(skill.getId())).isInstanceOf(Skill.class)
        .extracting(Skill::getId).isEqualTo(skill.getId());
  }

  @DisplayName("should throw IAE by getting the skill with non existing ID")
  @Test
  void testIfMethodThrowsIaeGettingTheSkillWithNonExistingId() {
    when(repository.findById(anyInt())).thenThrow(IllegalArgumentException.class);
    assertThatIllegalArgumentException().isThrownBy(() -> service.findById(anyInt()));
  }
}