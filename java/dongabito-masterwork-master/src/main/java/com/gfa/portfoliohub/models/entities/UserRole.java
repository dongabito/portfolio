package com.gfa.portfoliohub.models.entities;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserRole {
  ROLE_USER {
    @Override
    public SimpleGrantedAuthority getAuthority() {
      return new SimpleGrantedAuthority(this.name());
    }
  }, ROLE_ADMIN {
    @Override
    public SimpleGrantedAuthority getAuthority() {
      return new SimpleGrantedAuthority(this.name());
    }
  };

  public abstract SimpleGrantedAuthority getAuthority();
}
