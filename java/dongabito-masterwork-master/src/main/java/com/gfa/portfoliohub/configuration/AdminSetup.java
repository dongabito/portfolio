package com.gfa.portfoliohub.configuration;

import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.models.entities.UserRole;
import com.gfa.portfoliohub.repositories.ProgrammerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSetup implements ApplicationListener<ContextRefreshedEvent> {

  @Value("${adminsetup.firstname}")
  private String firstName;
  @Value("${adminsetup.lastname}")
  private String lastName;
  @Value("${adminsetup.email}")
  private String email;
  @Value("${adminsetup.secret}")
  private String secret;
  private PasswordEncoder encoder;
  private ProgrammerRepository programmerRepository;

  public AdminSetup(PasswordEncoder encoder,
                    ProgrammerRepository programmerRepository) {
    this.encoder = encoder;
    this.programmerRepository = programmerRepository;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (programmerRepository.existsById(1)) {
      return;
    }
    Programmer admin = Programmer.builder()
        .firstName(firstName)
        .lastName(lastName)
        .email(email)
        .password(encoder.encode(secret))
        .role(UserRole.ROLE_ADMIN)
        .build();
    programmerRepository.save(admin);
  }
}
