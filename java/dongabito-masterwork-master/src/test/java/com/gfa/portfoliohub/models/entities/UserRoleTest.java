package com.gfa.portfoliohub.models.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@DisplayName("Method ")
class UserRoleTest {

  @DisplayName("should return a SimpleGrantedAuthority object")
  @Test
  void testIfMethodReturnAAuthorityObject() {
    assertThat(UserRole.ROLE_USER.getAuthority()).isInstanceOf(SimpleGrantedAuthority.class);
  }

}