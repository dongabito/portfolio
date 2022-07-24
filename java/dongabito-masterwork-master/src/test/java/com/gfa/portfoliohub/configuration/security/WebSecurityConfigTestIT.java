package com.gfa.portfoliohub.configuration.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("WebSecurity ")
class WebSecurityConfigTestIT {

  @Autowired
  MockMvc mockMvc;

  @DisplayName("should login successful the user and send JWT token")
  @Test
  void testIfMethodLoginUser() throws Exception {
    String request = "{ "
        + "\"email\": \"foo@example.org\","
        + "\"password\": \"password\""
        + "}";

    mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("ok"))
        .andExpect(jsonPath("$.token").isNotEmpty());
  }

  @DisplayName("should throw ADE when input data are incorrect")
  @Test
  void testIfMethodThrowsADEByLoginUser() throws Exception {
    String request = "{ "
        + "\"email\": \"foo@example.org\","
        + "\"password\": \"passwordd\""
        + "}";

    mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
        .andExpect(status().isUnauthorized());
  }

  @DisplayName("should get Error message when token is not provided")
  @Test
  void testIfMethodGetErrorMessageWhenRequestIsUnauthorized() throws Exception {

    mockMvc.perform(MockMvcRequestBuilders.delete("/portfolio/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.type").value("/login"))
        .andExpect(jsonPath("$.title").value("Authentication failure"))
        .andExpect(jsonPath("$.status").value("401"));
  }

  @DisplayName("should get Error when User has not right to get the resource")
  @Test
  @WithMockUser
  void testIfMethodGetErrorMessageWhenUserNotAuthorized() throws Exception {

    mockMvc.perform(MockMvcRequestBuilders.delete("/skill/2")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.type").value("/authorization"))
        .andExpect(jsonPath("$.title").value("Authorization failure"))
        .andExpect(jsonPath("$.status").value("401"))
        .andExpect(jsonPath("$.detail").value("Access is denied"));
  }

}