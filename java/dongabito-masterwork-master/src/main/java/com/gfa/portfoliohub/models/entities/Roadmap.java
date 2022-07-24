package com.gfa.portfoliohub.models.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "roadmaps")
public class Roadmap {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  private String details;
  @LazyCollection(LazyCollectionOption.FALSE)
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinTable(name = "skills_roadmaps", joinColumns = @JoinColumn(name = "roadmap_id"),
      inverseJoinColumns = @JoinColumn(name = "skill_id"))
  private List<Skill> skills = new ArrayList<>();
  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "programmer_id")
  private Programmer programmer;
  @OneToMany(mappedBy = "roadmap", fetch = FetchType.EAGER)
  private List<ProgressInTechnology> progresses = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Roadmap roadmap = (Roadmap) o;
    return Objects.equals(id, roadmap.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
