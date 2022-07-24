package com.gfa.portfoliohub.models.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Method ")
class LearningProgressTest {

  @DisplayName(" should return with an appropriate css property for progress bar in HTML")
  @Test
  void testIfMethodReturnWithACssProperty() {
    assertThat(LearningProgress.BASIC.getProgress()).isEqualTo("width: 33%");
  }

}