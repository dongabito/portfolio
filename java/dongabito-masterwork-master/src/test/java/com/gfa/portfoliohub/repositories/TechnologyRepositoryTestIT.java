package com.gfa.portfoliohub.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("TechnologyRepo ")
@DataJpaTest
class TechnologyRepositoryTestIT {

  @Autowired
  TechnologyRepository repository;
  List<String> keywords;

  @BeforeEach
  void setUp() {
    keywords = repository.listAllTheKeywords();
  }

  @DisplayName("should return all the keywords")
  @Test
  void testIfMethodListAllTheKeywords() {
    assertThat(keywords).anyMatch(s -> s.equals("Maven Repo"));
  }
}