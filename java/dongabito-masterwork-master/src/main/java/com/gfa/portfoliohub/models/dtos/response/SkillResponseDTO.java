package com.gfa.portfoliohub.models.dtos.response;

import com.gfa.portfoliohub.models.entities.Skill;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkillResponseDTO {
  private List<SkillDTO> skills = new ArrayList<>();
}
