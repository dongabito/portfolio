package com.gfa.portfoliohub.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.gfa.portfoliohub.models.entities.Programmer;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("ProgrammerRepository ")
@DataJpaTest
class ProgrammerRepositoryTestIT {

  @Autowired
  private ProgrammerRepository repository;
  Programmer programmer;

  @BeforeEach
  void setUp() {
    programmer = repository.getById(1);
  }

  @DisplayName("should save a programmer entity")
  @Test
  void testIfMethodSaveTheEntity() {
    assertThat(repository.save(programmer)).isInstanceOf(Programmer.class);
  }

  @DisplayName("should return a programmer entity searched by own email as username")
  @Test
  void testIfMethodReturnTheEntitySearchedByEmailAsUsername() {
    assertThat(repository.findProgrammerByEmail(programmer.getEmail())).isEqualTo(
        Optional.of(programmer));
  }

  @DisplayName("should return a programmer with the given skill")
  @Test
  void testIfMethodFindBySkillNameReturnProgrammer() {
    assertThat(repository.findProgrammerBySkillId(1).get(0))
        .extracting(Programmer::getEmail).isEqualTo(programmer.getEmail());
  }

  @DisplayName("should return a programmer with the given technology")
  @Test
  void testIfMethodFindByTechnologyNameReturnProgrammer() {
    assertThat(repository.findProgrammerByTechnologyName("Eclipse").get(0))
        .extracting(Programmer::getEmail).isEqualTo(programmer.getEmail());
  }


  @DisplayName("should return a programmer with the given technology by ID")
  @Test
  void testIfMethodFindByTechnologyIDReturnProgrammer() {
    assertThat(repository.findProgrammerByTechnologyID(1).get(0))
        .extracting(Programmer::getEmail).isEqualTo(programmer.getEmail());
  }
}