package com.gfa.portfoliohub.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "cvs")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CurriculumVitae {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Size(min = 2)
  @NotBlank(message = "Name can not be null or empty")
  private String title;
  @Column(columnDefinition = "text")
  @Size(min = 2)
  @NotBlank(message = "Name can not be null or empty")
  private String content;
  private String imageSource;
  @OneToOne(mappedBy = "cv", cascade = CascadeType.ALL)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Programmer programmer;
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "social_presence", joinColumns = @JoinColumn(name = "cv_id"))
  @MapKeyColumn(name = "provider")
  @Column(name = "link")
  private Map<String, String> socialPresence = new HashMap<>();

  public CurriculumVitae() {

  }
}
