package com.gfa.portfoliohub.configuration.security.filters;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

  private final String jwtSecret;
  private final UserDetailsService userDetailsService;

  public CustomAuthorizationFilter(UserDetailsService userDetailsService,
                                   String jwtSecret) {
    this.userDetailsService = userDetailsService;
    this.jwtSecret = jwtSecret;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    if (request.getServletPath().equals("/login") || request.getServletPath().equals("/register")) {
      filterChain.doFilter(request, response);
    } else {
      String authorizationHeader = request.getHeader(AUTHORIZATION);
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        String token = authorizationHeader.substring("Bearer ".length());
        try {
          Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
          setSecContext(claims);
          filterChain.doFilter(request, response);
        } catch (Exception e) {
          response.sendError(FORBIDDEN.value());
        }
      } else {
        filterChain.doFilter(request, response);
      }
    }
  }

  private void setSecContext(Claims claims) {
    String username = claims.getSubject();
    List<String> roles = (List<String>) claims.get("authorities");
    UserDetails programmer = userDetailsService.loadUserByUsername(username);
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
        programmer, null, Arrays.asList(new SimpleGrantedAuthority(roles.get(0))));
    SecurityContextHolder.getContext().setAuthentication(auth);
  }
}
