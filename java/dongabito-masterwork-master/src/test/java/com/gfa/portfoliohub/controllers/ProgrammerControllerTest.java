package com.gfa.portfoliohub.controllers;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gfa.portfoliohub.models.dtos.response.ProgrammerDTO;
import com.gfa.portfoliohub.models.entities.CurriculumVitae;
import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.services.ProgrammerServiceImpl;
import com.gfa.portfoliohub.util.TestEntities;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@DisplayName("ProgrammerController ")
@ExtendWith(MockitoExtension.class)
class ProgrammerControllerTest {

  @Mock
  ProgrammerServiceImpl service;
  @InjectMocks
  ProgrammerController controller;
  @Captor
  ArgumentCaptor<Programmer> capturedUser;
  @Captor
  ArgumentCaptor<CurriculumVitae> capturedCV;

  Programmer programmer;
  ProgrammerDTO dto;
  Authentication auth;

  @BeforeEach
  void setUp() {
    ModelMapper mapper = new ModelMapper();
    programmer = TestEntities.defaultProgrammer();
    dto = mapper.map(programmer, ProgrammerDTO.class);
    auth = new UsernamePasswordAuthenticationToken(programmer,
        null, programmer.getAuthorities());
  }

  @DisplayName("should return the searched programmer")
  @Test
  void testIfMethodRetrievesTheGivenProgrammer() {
    Programmer programmerUnderTest = (Programmer) auth.getPrincipal();
    controller.getPortfolio(auth);
    verify(service).getProgrammerByID(argThat(num -> Objects.equals(num,
        programmerUnderTest.getId())));
  }

  @DisplayName("should throw IAE when Programmer does not exist")
  @Test
  void testIfMethodThrowsIAE() throws Exception {
    Integer userId = ((Programmer) auth.getPrincipal()).getId();
    when(service.getProgrammerByID(userId)).thenThrow(IllegalArgumentException.class);

    assertThatIllegalArgumentException().isThrownBy(() -> service.getProgrammerByID(userId));
  }

  @DisplayName("should add CV to programmers portfolio")
  @Test
  void testIfMethodAddCVToPortfolio() {
    Programmer programmerUnderTest = (Programmer) auth.getPrincipal();
    CurriculumVitae cv = TestEntities.defaultCV();
    controller.addCVToPortfolio(auth, cv);
    verify(service, times(1)).addCVToPortfolio(capturedUser.capture(), capturedCV.capture());
    assertEquals(programmerUnderTest, capturedUser.getValue());
    assertEquals(cv, capturedCV.getValue());
  }

  @DisplayName("should update CV of programmers portfolio")
  @Test
  void testIfMethodUpdateCVToPortfolio() {
    Programmer programmerUnderTest = (Programmer) auth.getPrincipal();
    CurriculumVitae cv = TestEntities.defaultCV();
    controller.updateCVToPortfolio(auth, cv);
    verify(service).addCVToPortfolio(capturedUser.capture(), capturedCV.capture());
    assertEquals(programmerUnderTest, capturedUser.getValue());
    assertEquals(cv, capturedCV.getValue());
  }

}