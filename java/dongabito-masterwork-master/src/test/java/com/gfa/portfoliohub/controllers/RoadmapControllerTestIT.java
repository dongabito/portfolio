package com.gfa.portfoliohub.controllers;

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
@DisplayName("Endpoint ")
class RoadmapControllerTestIT {

  @Autowired
  RoadmapController controller;
  @Autowired
  MockMvc mockMvc;

  Authentication auth;

  @Nested
  @DisplayName("Public endpoints ")
  class PublicEndpoint {

    @DisplayName("should return all roadmaps")
    @Test
    void testIfEndpointsReturnsAllRoadmaps() throws Exception {
      mockMvc.perform(get("/roadmap")
              .contentType("application/json"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.roadmaps").isArray());
    }

    @DisplayName("should return the roadmaps of given Programmer")
    @Test
    void testIfEndpointsReturnsAllRoadmapsOfGivenUser() throws Exception {
      mockMvc.perform(get("/roadmap?id=1")
              .contentType("application/json"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.roadmaps").isArray());
    }

    @DisplayName("should return the given roadmap")
    @Test
    void testIfEndpointsReturnsTheGivenRoadmap() throws Exception {
      mockMvc.perform(get("/roadmap/1")
              .contentType("application/json"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id", is(1)))
          .andExpect(jsonPath("$.programmer", is(1)));
    }
  }

  @Nested
  @DisplayName("Roadmap CRUD methods")
  class CrudEndpoint {

    @BeforeEach
    void setUp() {
      Programmer programmer = TestEntities.dbProgrammer();
      auth = TestEntities.defaultAuthentication(programmer);
    }

    @DisplayName("should return the given roadmap")
    @Test
    void testIfEndpointCreatesRoadmap() throws Exception {
      String request = "{ "
          + "\"name\": \"Java\","
          + "\"details\": \"Roadmap a Java nyelv megtanulásához\""
          + "}";

      mockMvc.perform(post("/roadmap")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON)
              .content(request))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.name", is("Java")))
          .andExpect(jsonPath("$.programmer", is(1)));
    }

    @DisplayName("should update the given roadmap")
    @Test
    void testIfEndpointUpdatesRoadmap() throws Exception {
      String request = "{ "
          + "\"id\": \"1\","
          + "\"name\": \"Changed\","
          + "\"details\": \"Roadmap a Java nyelv megtanulásához\""
          + "}";

      mockMvc.perform(put("/roadmap")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON)
              .content(request))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name", is("Changed")))
          .andExpect(jsonPath("$.programmer", is(1)));
    }

    @DisplayName("should delete the given roadmap")
    @Test
    void testIfEndpointDeletesRoadmap() throws Exception {
      ((Programmer) auth.getPrincipal()).getRoadmaps().add(TestEntities.dbRoadmap());
      mockMvc.perform(delete("/roadmap/1")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());
    }
  }

  @Nested
  @DisplayName("Methods handling users roadmaps ")
  class UsersEndpoint {

    @BeforeEach
    void setUp() {
      Programmer programmer = TestEntities.dbProgrammer();
      auth = TestEntities.defaultAuthentication(programmer);
    }

    @DisplayName("should add skill to the given roadmap")
    @Test
    void testIfEndpointAddSkillToRoadmap() throws Exception {
      ((Programmer) auth.getPrincipal()).getRoadmaps().add(TestEntities.dbRoadmap());
      mockMvc.perform(post("/roadmap/1/skill/3")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }

    @DisplayName("should delete skill from the given roadmap")
    @Test
    void testIfEndpointDeleteSkillFromRoadmap() throws Exception {
      ((Programmer) auth.getPrincipal()).getRoadmaps().add(TestEntities.dbRoadmap());
      ((Programmer) auth.getPrincipal()).getRoadmaps().get(0).getSkills()
          .add(TestEntities.dbSkill());

      mockMvc.perform(delete("/roadmap/1/skill/1")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }
  }

}