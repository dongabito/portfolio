package com.gfa.portfoliohub.models.dtos.response;

import com.gfa.portfoliohub.models.entities.CurriculumVitae;
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
public class ProgrammerCommonDTO {

  private Integer id;
  private String firstName;
  private String lastName;
  private CurriculumVitae cv;
  private List<SkillDTO> skills = new ArrayList<>();

  public void setSkills(List<Skill> skills) {
    this.skills = skills.stream().map(SkillDTO::new).collect(Collectors.toList());
  }
}
