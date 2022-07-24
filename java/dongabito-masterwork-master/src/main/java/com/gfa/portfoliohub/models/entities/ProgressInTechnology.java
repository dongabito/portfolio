package com.gfa.portfoliohub.models.entities;


import java.time.Instant;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "progresses")
public class ProgressInTechnology {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Enumerated(EnumType.STRING)
  private LearningProgress progress;
  private String details;
  private Instant startedAt;
  @ManyToOne
  @JoinColumn(name = "technology_id")
  private Technology technology;
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "roadmap_id")
  private Roadmap roadmap;

  public void setRoadmap(Roadmap roadmap) {
    this.roadmap = roadmap;
    roadmap.getProgresses().add(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgressInTechnology that = (ProgressInTechnology) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
