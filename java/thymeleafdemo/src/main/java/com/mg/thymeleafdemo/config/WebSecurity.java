package com.mg.thymeleafdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurity {

    @Value("${adminsetup.username}")
    private String username;
    @Value("${adminsetup.password}")
    private String password;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.GET, "/recipes", "/recipes/{id:\\d+}")
                .permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .and()
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().authenticated()
                )
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/recipes", true)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/recipes")
                .deleteCookies();
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username(username)
                .password(password)
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/db/**");
    }

}
