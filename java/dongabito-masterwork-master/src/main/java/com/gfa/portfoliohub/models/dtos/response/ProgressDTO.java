package com.gfa.portfoliohub.models.dtos.response;


import com.gfa.portfoliohub.models.entities.LearningProgress;
import com.gfa.portfoliohub.models.entities.ProgressInTechnology;
import com.gfa.portfoliohub.models.entities.Roadmap;
import com.gfa.portfoliohub.models.entities.Technology;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgressDTO {

  private Integer id;
  private LearningProgress progress;
  private String details;
  private Instant startedAt;
  private String technology;
  private Integer roadmap;

  public ProgressDTO(ProgressInTechnology progress) {
    this.id = progress.getId();
    this.progress = progress.getProgress();
    this.details = progress.getDetails();
    this.startedAt = progress.getStartedAt();
    this.technology = progress.getTechnology().getName();
    this.roadmap = progress.getRoadmap().getId();
  }
}
