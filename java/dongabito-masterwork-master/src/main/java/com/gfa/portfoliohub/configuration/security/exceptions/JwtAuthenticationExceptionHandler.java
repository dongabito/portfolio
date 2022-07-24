package com.gfa.portfoliohub.configuration.security.exceptions;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class JwtAuthenticationExceptionHandler implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException authException) throws IOException, ServletException {
    response.setContentType(MediaType.APPLICATION_PROBLEM_JSON.getType());
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getOutputStream()
        .println("{ \n\t\"type\": \"" + "/login" + "\",\n\t" +
            "\"title\": \"Authentication failure\",\n\t" +
            "\"status\": " + HttpServletResponse.SC_UNAUTHORIZED + ",\n\t" +
            "\"detail\": " + "\"" + authException.getMessage() + "\"\n" +
            "}");
  }
}
