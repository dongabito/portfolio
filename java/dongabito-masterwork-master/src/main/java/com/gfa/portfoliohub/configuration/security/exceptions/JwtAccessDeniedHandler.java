package com.gfa.portfoliohub.configuration.security.exceptions;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
                     AccessDeniedException accessDeniedException)
      throws IOException, ServletException {

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getOutputStream()
        .println("{ \n\t\"type\": \"" + "/authorization" + "\",\n\t" +
            "\"title\": \"Authorization failure\",\n\t" +
            "\"status\": " + HttpServletResponse.SC_UNAUTHORIZED + ",\n\t" +
            "\"detail\": " + "\"" + accessDeniedException.getMessage() + "\"\n" +
            "}");

  }
}