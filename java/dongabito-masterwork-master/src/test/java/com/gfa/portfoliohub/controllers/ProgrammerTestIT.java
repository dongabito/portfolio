package com.gfa.portfoliohub.controllers;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.models.entities.Roadmap;
import com.gfa.portfoliohub.models.entities.Skill;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestNoSecurityConfig.class)
public class ProgrammerTestIT {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  ProgrammerController controller;

  Authentication auth;

  @Nested
  @DisplayName("Public endpoints ")
  class PublicEndpoint {

    @Test
    @WithMockUser
    @DisplayName("should return with the given programmer")
    void testIfEndpointReturnWithProgrammerDTOOfTheGivenProgrammer() throws Exception {
      mockMvc.perform(get("/portfolio/2")
              .contentType("application/json"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.firstName").value("Gyula"));
    }

    @Test
    @DisplayName("should return a list of Programmers having the searched skill")
    void testIfEndpointReturnListOfProgrammersHavingTheGivenSkill() throws Exception {
      mockMvc.perform(get("/portfolio/skill/3")
              .contentType("application/json"))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should throw IAE searching with non valid data")
    void testIfEndpointThrowsIAEByNonValidDataOfSKill() throws Exception {
      mockMvc.perform(get("/portfolio/skill/100")
              .contentType("application/json"))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.title").value("Not an appropriate parameter"))
          .andExpect(jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    @DisplayName("should return a list of Programmers having the searched technology")
    void testIfEndpointReturnListOfProgrammersHavingTheGivenTechnology() throws Exception {
      mockMvc.perform(get("/portfolio/technology/4")
              .contentType("application/json"))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should throw IAE searching with non valid data")
    void testIfEndpointThrowsIAEByNonValidDataOfTechnology() throws Exception {
      mockMvc.perform(get("/portfolio/technology/100")
              .contentType("application/json"))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.title").value("Not an appropriate parameter"))
          .andExpect(jsonPath("$.status").value("NOT_FOUND"));
    }

    @DisplayName("should return all roadmaps")
    @Test
    void testIfEndpointsReturnsAllRoadmaps() throws Exception {
      mockMvc.perform(get("/roadmap")
              .contentType("application/json"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.roadmaps.*", hasSize(2)))
          .andExpect(jsonPath("$.roadmaps[0].id", is(1)));
    }

    @DisplayName("should return the roadmaps of given Programmer")
    @Test
    void testIfEndpointsReturnsAllRoadmapsOfGivenUser() throws Exception {
      mockMvc.perform(get("/roadmap?name=1")
              .contentType("application/json"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.roadmaps.*", hasSize(2)))
          .andExpect(jsonPath("$.roadmaps[0].id", is(1)));
    }

    @DisplayName("should return the given roadmap")
    @Test
    void testIfEndpointsReturnsTheGivenRoadmap() throws Exception {
      mockMvc.perform(get("/roadmap/2")
              .contentType("application/json"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id", is(2)))
          .andExpect(jsonPath("$.programmer", is(1)));
    }
  }

  @Nested
  @DisplayName("Endpoints handling CVs ")
  class CVEndpoint {

    @BeforeEach
    void setUp() {
      Programmer programmer = TestEntities.dbProgrammer();
      auth = TestEntities.defaultAuthentication(programmer);
    }

    @DisplayName("should add cv to programmer")
    @Test
    void testIfEndpointAddCVToProgrammer() throws Exception {
      String request = "{ "
          + "\"content\": \"I was born...\","
          + "\"imageSource\": \"http://example.org/img\","
          + "\"socialPresence\": { \"GitHub\": \"GitHub\" },"
          + "\"title\": \"Its my life, tü-rü-rü\""
          + "}";

      mockMvc.perform(post("/portfolio/cv")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON)
              .content(request))
          .andExpect(status().isCreated());
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

  @Nested
  @DisplayName("Endpoints handling users skills ")
  class SkillHandlerEndpoint {

    @BeforeEach
    void setUp() {
      Programmer programmer = TestEntities.dbProgrammer();
      auth = TestEntities.defaultAuthentication(programmer);
    }

    @DisplayName("should add skill to programmer")
    @Test
    void testIfEndpointAddSkillToProgrammer() throws Exception {
      mockMvc.perform(post("/portfolio/skill/4")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.skills[0].id", is(4)))
          .andExpect(jsonPath("$.id", is(1)));
    }

    @DisplayName("should throw IAE while adding skill to programmer")
    @Test
    void testIfEndpointThrowsIAEAddingSkillToProgrammer() throws Exception {
      mockMvc.perform(post("/portfolio/skill/400")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.title", is("Not an appropriate parameter")))
          .andExpect(jsonPath("$.status", is("NOT_FOUND")));
    }

    @DisplayName("should remove skill from programmer")
    @Test
    void testIfEndpointRemoveSkillFromProgrammer() throws Exception {
      mockMvc.perform(delete("/portfolio/skill/4")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());
    }

    @DisplayName("should throw OBCE by removing skill from programmer")
    @Test
    void testIfEndpointThrowsOBCEByRemovingSkillFromProgrammer() throws Exception {
      ((Programmer) auth.getPrincipal()).getRoadmaps().add(TestEntities.dbRoadmap());
      ((Programmer) auth.getPrincipal()).getRoadmaps().get(0).getSkills()
          .add(TestEntities.dbSkill());
      mockMvc.perform(delete("/portfolio/skill/1")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isConflict())
          .andExpect(jsonPath("$.title", is("Operation depends on other objects")))
          .andExpect(jsonPath("$.status", is("CONFLICT")));
    }
  }

  @Nested
  @DisplayName("Endpoints handling users learning progress ")
  class ProgressHandlerEndpoint {

    @BeforeEach
    void setUp() {
      Programmer programmer = TestEntities.dbProgrammer();
      auth = TestEntities.defaultAuthentication(programmer);
      Skill skill = TestEntities.dbSkill();
      Roadmap roadmap = TestEntities.dbRoadmap();
      roadmap.getProgresses().add(TestEntities.dbProgress());
      ((Programmer) auth.getPrincipal()).getRoadmaps().add(roadmap);
      ((Programmer) auth.getPrincipal()).getRoadmaps().get(0).getSkills().add(skill);
    }

    @DisplayName("should add progress to programmers roadmap")
    @Test
    void testIfEndpointAddProgressToProgrammersRoadmap() throws Exception {
      String request = "{ "
          + "\"learningProgress\": \"BEGINNER\","
          + "\"details\": \"Bla-bla\","
          + "\"startedAt\": 1654874250,"
          + "\"technology_id\": 1,"
          + "\"roadmap_id\": 1"
          + "}";

      mockMvc.perform(post("/portfolio/technology/progress")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON)
              .content(request))
          .andExpect(status().isCreated())
          .andDo(print());
    }

    @DisplayName("should throw IAE by adding progress to programmers roadmap")
    @Test
    void testIfEndpointThrowsIAEProgressToProgrammersRoadmap() throws Exception {
      String request = "{ "
          + "\"learningProgress\": \"BEGINNER\","
          + "\"details\": \"Bla-bla\","
          + "\"startedAt\": 1654874250,"
          + "\"technology_id\": 2,"
          + "\"roadmap_id\": 1"
          + "}";

      mockMvc.perform(post("/portfolio/technology/progress")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON)
              .content(request))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.title", is("Not an appropriate parameter")))
          .andExpect(jsonPath("$.status", is("NOT_FOUND")));
    }

    @DisplayName("should update progress to programmers roadmap")
    @Test
    void testIfEndpointUpdateProgressToProgrammersRoadmap() throws Exception {
      String request = "{ "
          + "\"learningProgress\": \"BEGINNER\","
          + "\"details\": \"Bla-bla_bla\","
          + "\"startedAt\": 1654874250,"
          + "\"technology_id\": 5,"
          + "\"roadmap_id\": 1"
          + "}";

      mockMvc.perform(put("/portfolio/technology/progress/5")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON)
              .content(request))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.skills[0].id").value(1));
    }

    @DisplayName("should remove progress from programmers roadmap")
    @Test
    void testIfEndpointRemoveProgressFromProgrammersRoadmap() throws Exception {
      mockMvc.perform(delete("/portfolio/technology/progress/2")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());
    }

    @DisplayName("should throw IAE removing progress from programmers roadmap")
    @Test
    void testIfEndpointThrowsIAEByRemovingProgressFromProgrammersRoadmap() throws Exception {
      mockMvc.perform(delete("/portfolio/technology/progress/3")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.title", is("Not an appropriate parameter")))
          .andExpect(jsonPath("$.status", is("NOT_FOUND")))
          .andExpect(jsonPath("$.detail", is("User has not such a progress")));
    }

    @DisplayName("should throw NFE removing progress from programmers roadmap with not a numeric value")
    @Test
    void testIfEndpointThrowsIAEByRemovingProgressFromProgrammersRoadmapWithNonNumber()
        throws Exception {
      mockMvc.perform(delete("/portfolio/technology/progress/a")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.title", is("Not an appropriate parameter")))
          .andExpect(jsonPath("$.status", is("NOT_FOUND")))
          .andExpect(jsonPath("$.detail", startsWith("For input string:")));
    }

    @DisplayName("should throw MNA removing progress from programmers roadmap with a not appropriate request method")
    @Test
    void testIfEndpointThrowsIAEByRemovingProgressFromProgrammersRoadmapWithWrongMethod()
        throws Exception {
      mockMvc.perform(get("/portfolio/technology/progress/1")
              .principal(auth)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isMethodNotAllowed());
    }
  }
}
