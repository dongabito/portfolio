package com.gfa.portfoliohub.configuration.security.filters;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final String jwtSecret;
  private final AuthenticationManager authenticationManager;
  private static final int EXPIRATION = 30 * 60 * 1000;


  public CustomAuthenticationFilter(
      AuthenticationManager authenticationManager, String jwtSecret) {
    this.authenticationManager = authenticationManager;
    this.jwtSecret = jwtSecret;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
                                              HttpServletResponse response)
      throws AuthenticationException {
    String requestBody = "";
    try {
      requestBody = request.getReader().lines().reduce("", String::concat);
    } catch (IOException e) {
      e.printStackTrace();
    }
    String[] data = requestBody.split("\"");
    String email = data[3];
    String password = data[7];
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(email, password);
    return authenticationManager.authenticate(authenticationToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                          FilterChain chain, Authentication authResult)
      throws IOException, ServletException {
    long now = System.currentTimeMillis();
    String access_token = Jwts.builder()
        .setSubject(authResult.getName())
        .claim("authorities", authResult.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
        .setIssuedAt(new Date(now))
        .setExpiration(new Date(now + EXPIRATION))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getOutputStream()
        .println("{ \n" +
            "\t\"status\": \"ok\",\n" +
            "\t\"token\": \"" + access_token + "\"\n" +
            "}");
  }

}
