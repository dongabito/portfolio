package com.gfa.portfoliohub.controllers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gfa.portfoliohub.models.dtos.response.ProgrammerDTO;
import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.models.entities.UserRole;
import com.gfa.portfoliohub.services.ProgrammerService;
import com.gfa.portfoliohub.services.ProgressInTechnologyService;
import com.gfa.portfoliohub.util.TestEntities;
import com.gfa.portfoliohub.util.TestNoSecurityConfig;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@DisplayName("ProgrammerController ")
@WebMvcTest(ProgrammerController.class)
@Import(TestNoSecurityConfig.class)
class ProgrammerControllerTestIT {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  ProgrammerService services;
  @MockBean
  ProgressInTechnologyService progress;
  @MockBean
  UserDetailsService userDetailsService;

  Authentication auth;
  Authentication adminAuth;
  Programmer user;
  ProgrammerDTO dto;

  @BeforeEach
  void setUp() {
    ModelMapper mapper = new ModelMapper();
    user = TestEntities.defaultProgrammer();
    auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    adminAuth = new UsernamePasswordAuthenticationToken(user, null,
        Arrays.asList(UserRole.ROLE_ADMIN.getAuthority()));
    dto = mapper.map(user, ProgrammerDTO.class);
  }

  @DisplayName("should return the searched Programmer")
  @Test
  void testIfMethodReturnProgrammer() throws Exception {
    when(services.getProgrammerByID(anyInt())).thenReturn(dto);

    mockMvc.perform(MockMvcRequestBuilders.get("/portfolio").principal(auth))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("foo@example.hu"));
  }

  @DisplayName("should return the searched Programmer by Id")
  @Test
  void testIfMethodReturnProgrammerById() throws Exception {
    when(services.getProgrammerByID(anyInt())).thenReturn(dto);
    mockMvc.perform(MockMvcRequestBuilders.get("/portfolio/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("foo@example.hu"));
  }

}