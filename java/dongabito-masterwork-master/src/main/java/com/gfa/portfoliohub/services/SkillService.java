package com.gfa.portfoliohub.services;

import com.gfa.portfoliohub.models.dtos.response.SkillCategoryResponseDto;
import com.gfa.portfoliohub.models.dtos.response.SkillDTO;
import com.gfa.portfoliohub.models.dtos.response.SkillResponseDTO;
import com.gfa.portfoliohub.models.entities.Skill;

public interface SkillService {

  SkillResponseDTO findAllTheSkills();

  SkillCategoryResponseDto getAllTheSkillCategory();

  SkillDTO save(Skill request);

  void deleteSkill(Integer id) throws IllegalArgumentException;

  Skill findById(Integer id) throws IllegalArgumentException;
}

