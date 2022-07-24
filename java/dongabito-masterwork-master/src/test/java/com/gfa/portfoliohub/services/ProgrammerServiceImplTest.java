package com.gfa.portfoliohub.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gfa.portfoliohub.exceptions.OperationBlockedByConstraintsException;
import com.gfa.portfoliohub.models.dtos.response.ProgrammerCommonDTO;
import com.gfa.portfoliohub.models.dtos.response.ProgrammerDTO;
import com.gfa.portfoliohub.models.dtos.response.ProgrammerResponseDTO;
import com.gfa.portfoliohub.models.entities.CurriculumVitae;
import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.models.entities.Skill;
import com.gfa.portfoliohub.models.entities.UserRole;
import com.gfa.portfoliohub.repositories.ProgrammerRepository;
import com.gfa.portfoliohub.util.TestEntities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayName("ProgrammerService ")
@ExtendWith(MockitoExtension.class)
class ProgrammerServiceImplTest {

  @Mock
  ProgrammerRepository repository;
  @Mock
  ModelMapper mapper;
  @Mock
  PasswordEncoder passwordEncoder;
  @Mock
  SkillService skillService;

  @InjectMocks
  ProgrammerServiceImpl services;

  Programmer programmer;
  ProgrammerCommonDTO dto;
  ProgrammerDTO dto2;
  ProgrammerResponseDTO dtoList;

  @BeforeEach
  void setUp() {
    ModelMapper modelMapper = new ModelMapper();
    passwordEncoder = new BCryptPasswordEncoder();
    programmer = TestEntities.defaultProgrammer();
    dto = modelMapper.map(programmer, ProgrammerCommonDTO.class);
    dto2 = modelMapper.map(programmer, ProgrammerDTO.class);
    dtoList = new ProgrammerResponseDTO(Arrays.asList(dto));
  }

  @DisplayName("should return with the given programmerDTO searched by ID")
  @Test
  void testIfMethodReturnWithGivenProgrammerDTOSearchedByID() {
    SecurityContext secContext = new SecurityContext() {
      @Override
      public Authentication getAuthentication() {
        return new UsernamePasswordAuthenticationToken(programmer, null, Arrays.asList(
            UserRole.ROLE_USER.getAuthority()));
      }

      @Override
      public void setAuthentication(Authentication authentication) {

      }
    };
    try
        (MockedStatic<SecurityContextHolder> mockedSecContext = Mockito.mockStatic(
            SecurityContextHolder.class)) {
      mockedSecContext.when(SecurityContextHolder::getContext).thenReturn(secContext);
      when(repository.findById(1)).thenReturn(Optional.of(programmer));
      when(mapper.map(programmer, ProgrammerDTO.class)).thenReturn(dto2);
      assertThat(services.getProgrammerByID(1)).isInstanceOf(ProgrammerDTO.class)
          .extracting("firstName").isEqualTo("John");
    }
  }

  @DisplayName("should return with the given programmerDTO searched by ID with anonymous User")
  @Test
  void testIfMethodReturnWithGivenProgrammerDTOSearchedByIDWithAnonymousUser() {
    SecurityContext secContext = new SecurityContext() {
      @Override
      public Authentication getAuthentication() {
        return new UsernamePasswordAuthenticationToken("anonymousUser", null, null);
      }

      @Override
      public void setAuthentication(Authentication authentication) {

      }
    };
    try
        (MockedStatic<SecurityContextHolder> mockedSecContext = Mockito.mockStatic(
            SecurityContextHolder.class)) {
      mockedSecContext.when(SecurityContextHolder::getContext).thenReturn(secContext);
      when(repository.findById(1)).thenReturn(Optional.of(programmer));
      when(mapper.map(programmer, ProgrammerCommonDTO.class)).thenReturn(dto);
      assertThat(services.getProgrammerByID(1)).isInstanceOf(ProgrammerCommonDTO.class)
          .extracting("id").isEqualTo(programmer.getId());
    }
  }

  @DisplayName("should return with the given programmerDTO searched by skill ID")
  @Test
  void testIfMethodReturnWithGivenProgrammerDTOSearchedBySKillID() {
    when(repository.findProgrammerBySkillId(anyInt())).thenReturn(Arrays.asList(programmer));
    when(mapper.map(programmer, ProgrammerCommonDTO.class)).thenReturn(dto);
    assertThat(services.getProgrammerBySkillID(anyInt())).isInstanceOf(ProgrammerResponseDTO.class);
  }

  @DisplayName("should throw IAE when programmer searched by skill not existing ID")
  @Test
  void testIfMethodThrowsIaeWhenProgrammerSearchedByNonExistingSKillID() {
    when(repository.findProgrammerBySkillId(anyInt())).thenReturn(new ArrayList<>());
    assertThatIllegalArgumentException().isThrownBy(() -> services.getProgrammerBySkillID(1000))
        .withMessage("Not an existing Skill ID");
  }

  @DisplayName("should save a programmer")
  @Test
  void testIfMethodSaveTheEntity() {
    services.saveProgrammer(programmer);
    ArgumentCaptor<Programmer> geekCaptor = ArgumentCaptor.forClass(Programmer.class);
    verify(repository, times(1)).save(geekCaptor.capture());
    assertThat(geekCaptor.getValue()).isEqualTo(programmer);
  }

  @DisplayName("should delete a programmer")
  @Test
  void testIfMethodDeleteTheEntity() {
    services.deleteProgrammer(programmer.getId());
    verify(repository).deleteById(anyInt());
  }

  @DisplayName("should add the CV to the Programmer")
  @Test
  void testIfMethodAddCvToProgrammer() {
    CurriculumVitae cv = TestEntities.defaultCV();
    cv.setTitle("ÉN");
    when(repository.save(any(Programmer.class))).thenReturn(programmer);
    assertThat(services.addCVToPortfolio(programmer, cv)).isInstanceOf(CurriculumVitae.class)
        .extracting(CurriculumVitae::getTitle).isEqualTo("ÉN");
  }

  @DisplayName("should return true by existing entity")
  @Test
  void testIfMethodReturnTrueIfExistsTheProgrammer() {
    when(repository.existsById(programmer.getId())).thenReturn(true);
    assertTrue(services.existsProgrammer(programmer));
  }

  @DisplayName("should add skill to portfolio")
  @Test
  void testIfMethodAddsSkillToPortfolio() {
    Skill skillToSave = TestEntities.dbSkill();
    skillToSave.getProgrammer().add(programmer);
    when(skillService.findById(anyInt())).thenReturn(skillToSave);
    ProgrammerDTO programmerDTO = services.addSkillToPortfolio(TestEntities.dbProgrammer(), 1);
    assertThat(programmerDTO).isInstanceOf(ProgrammerDTO.class);
  }

  @DisplayName("should remove skill from portfolio")
  @Test
  void testIfMethodRemovesSkillFromPortfolio() {
    when(skillService.findById(anyInt())).thenReturn(new Skill());
    services.removeSkillFromPortfolio(programmer, 1);
    verify(repository).save(argThat(p -> p.equals(programmer)));
  }

  @DisplayName("should throw OBCE")
  @Test
  void testIfMethodThrowsOBCE() {
    when(skillService.findById(anyInt())).thenReturn(programmer.getRoadmaps().get(0).getSkills()
        .get(0));
    verify(repository, never()).save(argThat(p -> p.equals(programmer)));
    assertThatThrownBy(() -> services.removeSkillFromPortfolio(programmer, 1))
        .isInstanceOf(OperationBlockedByConstraintsException.class)
        .hasMessage("First remove Roadmaps having this skill");
  }

  @DisplayName("should check if skill belongs to any roadmap of user")
  @Test
  void testIfMethodReturnTrueIfSkillBelongsToUsersRoadmap() {
    when(skillService.findById(anyInt())).thenReturn(programmer.getRoadmaps().get(0).getSkills()
        .get(0));
    assertTrue(services.checkIfBelongsToRoadmap(programmer, 1));
  }

  @DisplayName("should return with the given programmerDTO searched by technology ID")
  @Test
  void testIfMethodReturnWithGivenProgrammerDTOSearchedByTechnologyID() {
    when(repository.findProgrammerByTechnologyID(anyInt())).thenReturn(
        Arrays.asList(programmer));
    when(mapper.map(programmer, ProgrammerCommonDTO.class)).thenReturn(dto);
    assertThat(services.getProgrammerByTechnologyID(anyInt())).isInstanceOf(
        ProgrammerResponseDTO.class);
  }

  @DisplayName("should throw IAE when programmer searched by technology not existing ID")
  @Test
  void testIfMethodThrowsIaeWhenProgrammerSearchedByNonExistingTechnologyID() {
    when(repository.findProgrammerByTechnologyID(anyInt())).thenReturn(new ArrayList<>());
    assertThatIllegalArgumentException().isThrownBy(
            () -> services.getProgrammerByTechnologyID(1000))
        .withMessage("Not an existing Technology ID");
  }

  @DisplayName("should load the userdetails by email")
  @Test
  void testIfMethodReturnWithProperUserdetails() {
    when(repository.findProgrammerByEmail(any(String.class))).thenReturn(Optional.of(programmer));
    UserDetails user = services.loadUserByUsername("foo@example.org");
    verify(repository).save(any(Programmer.class));
    assertThat(user).isInstanceOf(UserDetails.class).extracting(UserDetails::getAuthorities)
        .isEqualTo(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
  }

  @DisplayName("should throw exception loading userdetails by email")
  @Test
  void testIfMethodThrowsExceptionLoadingUserdetails() {
    when(repository.findProgrammerByEmail(any(String.class))).thenThrow(
        UsernameNotFoundException.class);
    verify(repository, never()).save(any(Programmer.class));
    assertThatThrownBy(() -> services.loadUserByUsername("foo@example.org"))
        .isInstanceOf(UsernameNotFoundException.class);
  }

}