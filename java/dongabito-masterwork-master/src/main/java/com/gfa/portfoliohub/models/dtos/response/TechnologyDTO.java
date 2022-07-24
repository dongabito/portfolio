package com.gfa.portfoliohub.models.dtos.response;

import com.gfa.portfoliohub.models.entities.LearningProgress;
import com.gfa.portfoliohub.models.entities.Skill;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnologyDTO {
  private Integer id;
  private String name;
  private String description;
  private List<String> skills;
  private LearningProgress progress;
  private Set<String> keyWord;

  public void setSkills(List<Skill> skills) {
    if (skills == null) {
      skills = new ArrayList<>();
    } else {
      this.skills = skills.stream().map(Skill::getName).collect(Collectors.toList());
    }
  }
}
