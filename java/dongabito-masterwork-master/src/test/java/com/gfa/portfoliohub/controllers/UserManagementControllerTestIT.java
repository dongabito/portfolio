package com.gfa.portfoliohub.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gfa.portfoliohub.util.TestNoSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
@Import(TestNoSecurityConfig.class)
@DisplayName("User management controller ")
class UserManagementControllerTestIT {

  @Autowired
  ProgrammerController controller;
  @Autowired
  MockMvc mockMvc;

  @DisplayName("should register an user")
  @Test
  void testIfMethodRegisterANewUser() throws Exception {
    String request = "{ "
        + "\"firstName\": \"Gyula\","
        + "\"lastName\": \"Vitéz\","
        + "\"email\": \"foo@example.it\","
        + "\"password\": \"password\""
        + "}";

    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.firstName").value("Gyula"))
        .andExpect(jsonPath("$.password").doesNotExist());
  }

  @DisplayName("does not register an user with invalid input data")
  @Test
  void testIfMethodRegisterANewUserWithInvalidInputData() throws Exception {
    String request = "{ "
        + "\"firstName\": \"Gyula\","
        + "\"lastName\": \"Vitéz\","
        + "\"email\": \"foo@example.it\","
        + "\"password\": \"passw\""
        + "}";

    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.title").value("Not found"));
  }

  @DisplayName("should delete the given user")
  @Test
  void testIfEndpointDeletesUser() throws Exception {
    mockMvc.perform(delete("/portfolio/2")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

}