package com.gfa.portfoliohub.controllers;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.util.TestEntities;
import com.gfa.portfoliohub.util.TestNoSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestNoSecurityConfig.class)
@DisplayName("Technology endpoints")
class TechnologyControllerTestIT {

  @Autowired
  TechnologyController controller;
  @Autowired
  MockMvc mockMvc;

  Authentication auth;

  @Nested
  @DisplayName("Public endpoints")
  class PublicEndpoint {

    @DisplayName("should return all technologies")
    @Test
    void testIfEndpointReturnsAllTechnologies() throws Exception {
      mockMvc.perform(get("/technology")
              .contentType("application/json"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.technologies").isArray());
    }

    @DisplayName("should return the keywords")
    @Test
    void testIfEndpointsReturnsTheKeywords() throws Exception {
      mockMvc.perform(get("/technology/keywords")
              .contentType("application/json"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.*", hasItem("POM XML")));
    }

  }

  @Nested
  @DisplayName("Technology CRUD methods")
  class CrudEndpoint {

    @BeforeEach
    void setUp() {
      Programmer programmer = TestEntities.dbProgrammer();
      auth = TestEntities.defaultAuthentication(programmer);
    }

    @DisplayName("should add the given technology")
    @Test
    void testIfEndpointCreatesSkill() throws Exception {
      String request = "{ "
          + "\"name\": \"GradleW\","
          + "\"description\": \"Building process with Gradle\""
          + "}";

      mockMvc.perform(post("/technology")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON)
              .content(request))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.name").value("GradleW"));
    }

    @DisplayName("should update the given technology")
    @Test
    void testIfEndpointUpdatesTechnology() throws Exception {
      String request = "{ "
          + "\"id\": \"1\","
          + "\"name\": \"Changed\","
          + "\"description\": \"Building process with Gradle\""
          + "}";

      mockMvc.perform(put("/technology")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON)
              .content(request))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name", is("Changed")));
    }

    @DisplayName("should delete the given technology")
    @Test
    void testIfEndpointDeletesTechnology() throws Exception {
      mockMvc.perform(delete("/technology/6")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());
    }
  }

  @Nested
  @DisplayName("Methods handling users technologies ")
  class UsersEndpoint {

    @BeforeEach
    void setUp() {
      Programmer programmer = TestEntities.dbProgrammer();
      auth = TestEntities.defaultAuthentication(programmer);
    }

    @DisplayName("should add technology to the given skill")
    @Test
    void testIfEndpointAddTechnologyToSkill() throws Exception {
      mockMvc.perform(post("/technology/1/skill/4")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }

    @DisplayName("should delete technology from the given skill")
    @Test
    void testIfEndpointDeleteTechnologyFromSkill() throws Exception {
      mockMvc.perform(delete("/technology/1/skill/1")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());
    }
  }

}