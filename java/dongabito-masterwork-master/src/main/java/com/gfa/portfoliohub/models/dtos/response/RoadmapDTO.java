package com.gfa.portfoliohub.models.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.models.entities.ProgressInTechnology;
import com.gfa.portfoliohub.models.entities.Roadmap;
import com.gfa.portfoliohub.models.entities.Skill;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoadmapDTO {

  private Integer id;
  private String name;
  private String details;
  private List<SkillDTO> skills = new ArrayList<>();
  private Programmer programmer;
  private List<ProgressDTO> progresses = new ArrayList<>();

  public RoadmapDTO(Roadmap roadmap) {
    this.id = roadmap.getId();
    this.name = roadmap.getName();
    this.details = roadmap.getDetails();
    this.setSkills(roadmap.getSkills());
    this.programmer = roadmap.getProgrammer();
    this.setProgresses(roadmap.getProgresses());
  }

  public void setSkills(List<Skill> skills) {
    if (skills == null) {
      return;
    }
    this.skills = skills.stream()
        .map(SkillDTO::new)
        .collect(Collectors.toList());
  }

  public void setProgresses(
      List<ProgressInTechnology> progresses) {
    if (progresses == null) {
      this.progresses = new ArrayList<>();
    } else {
      this.progresses = progresses.stream()
          .map(ProgressDTO::new)
          .collect(Collectors.toList());
    }
  }
}
