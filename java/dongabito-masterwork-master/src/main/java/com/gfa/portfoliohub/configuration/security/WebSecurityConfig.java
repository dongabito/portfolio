package com.gfa.portfoliohub.configuration.security;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import com.gfa.portfoliohub.configuration.security.exceptions.JwtAccessDeniedHandler;
import com.gfa.portfoliohub.configuration.security.exceptions.JwtAuthenticationExceptionHandler;
import com.gfa.portfoliohub.configuration.security.filters.CustomAuthenticationFilter;
import com.gfa.portfoliohub.configuration.security.filters.CustomAuthorizationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  @Value("${security.jwt.secret}")
  private String jwtSecret;
  private final PasswordEncoder passwordEncoder;

  public WebSecurityConfig(
      UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder) {
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(POST, "/register", "/login").permitAll()
        .antMatchers(GET, "/technology/keywords", "/technology", "/roadmap", "/roadmap/**",
            "/skill/skillcategory", "/skill", "/portfolio/**",
            "/api/**").permitAll()
        .antMatchers(POST, "/technology/**").hasRole("ADMIN")
        .antMatchers(PUT, "/technology/**").hasRole("ADMIN")
        .antMatchers(DELETE, "/technology/**").hasRole("ADMIN")
        .antMatchers(POST, "/skill/**").hasRole("ADMIN")
        .antMatchers(PUT, "/skill/**").hasRole("ADMIN")
        .antMatchers(DELETE, "/skill/**").hasRole("ADMIN")
        .antMatchers("/**").hasAnyRole("USER", "ADMIN")
        .and()
        .exceptionHandling().authenticationEntryPoint(new JwtAuthenticationExceptionHandler())
        .and()
        .exceptionHandling().accessDeniedHandler(new JwtAccessDeniedHandler())
        .and()
        .addFilter(new CustomAuthenticationFilter(authenticationManagerBean(), jwtSecret))
        .addFilterBefore(new CustomAuthorizationFilter(userDetailsService, jwtSecret),
            UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

}
