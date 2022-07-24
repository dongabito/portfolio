package com.gfa.portfoliohub.models.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity(name = "programmers")
@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
@JsonIdentityReference(alwaysAsId = true)
public class Programmer implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Size(min = 2)
  @NotBlank(message = "Name can not be null or empty")
  private String firstName;
  @Size(min = 2)
  @NotBlank(message = "Name can not be null or empty")
  private String lastName;
  @Email(message = "Not a valid parameter")
  @Column(unique = true)
  private String email;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(min = 8)
  @NotBlank(message = "Password must have min. 8 characters")
  private String password;
  @Column(updatable = false)
  @CreationTimestamp
  private Instant createdAt;
  private Instant lastLogin;
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "cv_id")
  private CurriculumVitae cv;
  @LazyCollection(LazyCollectionOption.FALSE)
  @ManyToMany
  @JoinTable(name = "skills_programmers", joinColumns = @JoinColumn(name = "programmer_id"),
      inverseJoinColumns = @JoinColumn(name = "skill_id"))
  private List<Skill> skills = new ArrayList<>();
  @JsonIgnore
  @Enumerated(EnumType.STRING)
  private UserRole role;
  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "programmer", cascade = CascadeType.REMOVE)
  private List<Roadmap> roadmaps = new ArrayList<>();

  public Programmer() {
  }

  public void addSkillToList(Skill skill) {
    skills.add(skill);
    skill.getProgrammer().add(this);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.asList(role == null ? UserRole.ROLE_USER.getAuthority() : role.getAuthority());
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
