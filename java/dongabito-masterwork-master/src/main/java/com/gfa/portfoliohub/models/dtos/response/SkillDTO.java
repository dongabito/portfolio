package com.gfa.portfoliohub.models.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.models.entities.Skill;
import com.gfa.portfoliohub.models.entities.SkillCategory;
import com.gfa.portfoliohub.models.entities.Technology;
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
public class SkillDTO {
  private Integer id;
  private String name;
  private SkillCategory skillCategory;
  private List<String> technologies;
  private List<Programmer> programmerId;

  public SkillDTO(Skill skill) {
    this.id = skill.getId();
    this.name = skill.getName();
    this.skillCategory = skill.getSkillCategory();
    this.technologies = skill.getTechnologies().stream().map(Technology::getName).collect(Collectors.toList());
    this.programmerId = skill.getProgrammer();
  }

  public void setTechnologies(List<Technology> technologies) {
    if (technologies == null) {
      this.technologies = new ArrayList<>();
    } else {
      this.technologies =
          technologies.stream().map(Technology::getName).collect(Collectors.toList());
    }
  }
}
