package com.gfa.portfoliohub.controllers;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
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
@DisplayName("Skill endpoints")
class SkillControllerTestIT {

  @Autowired
  SkillController controller;
  @Autowired
  MockMvc mockMvc;

  Authentication auth;

  @Nested
  @DisplayName("Public endpoints ")
  class PublicEndpoint {

    @DisplayName("should return all skills")
    @Test
    void testIfEndpointReturnsAllSkills() throws Exception {
      mockMvc.perform(get("/skill")
              .contentType("application/json"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.skills").isArray());
    }

    @DisplayName("should return the skill categories")
    @Test
    void testIfEndpointsReturnsTheSkillCategories() throws Exception {
      mockMvc.perform(get("/skill/skillcategory")
              .contentType("application/json"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.*", hasItem("JAVA")));
    }
  }

  @Nested
  @DisplayName("Skill CRUD methods")
  class CrudEndpoint {

    @BeforeEach
    void setUp() {
      Programmer programmer = TestEntities.dbProgrammer();
      auth = TestEntities.defaultAuthentication(programmer);
    }

    @DisplayName("should add the given skill")
    @Test
    void testIfEndpointCreatesSkill() throws Exception {
      String request = "{ "
          + "\"name\": \"Javax\","
          + "\"skillCategory\": \"PROGRAMMING_LANGUAGE\""
          + "}";

      mockMvc.perform(post("/skill")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON)
              .content(request))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.name", is("Javax")));
    }

    @DisplayName("should throw SVE by adding an existing skill")
    @Test
    void testIfEndpointThrowsSVECreatingWithExistingSkill() throws Exception {
      String request = "{ "
          + "\"name\": \"IDE\","
          + "\"skillCategory\": \"PROGRAMMING_LANGUAGE\""
          + "}";

      mockMvc.perform(post("/skill")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON)
              .content(request))
          .andExpect(status().isConflict())
          .andExpect(jsonPath("$.title").value("SQL constraint violation"))
          .andExpect(jsonPath("$.status").value("CONFLICT"))
          .andExpect(jsonPath("$.detail", startsWith("Unique index or primary key violation:")));
    }

    @DisplayName("should update the given skill")
    @Test
    void testIfEndpointUpdatesSkill() throws Exception {
      String request = "{ "
          + "\"id\": \"1\","
          + "\"name\": \"Changed\","
          + "\"skillCategory\": \"PROGRAMMING_LANGUAGE\""
          + "}";

      mockMvc.perform(put("/skill")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON)
              .content(request))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name", is("Changed")));
    }

    @DisplayName("should delete the given skill")
    @Test
    void testIfEndpointDeletesSkill() throws Exception {
      mockMvc.perform(delete("/skill/1")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());
    }
  }

}