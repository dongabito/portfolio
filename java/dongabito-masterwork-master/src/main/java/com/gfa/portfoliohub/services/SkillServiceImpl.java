package com.gfa.portfoliohub.services;

import com.gfa.portfoliohub.models.dtos.response.SkillCategoryResponseDto;
import com.gfa.portfoliohub.models.dtos.response.SkillDTO;
import com.gfa.portfoliohub.models.dtos.response.SkillResponseDTO;
import com.gfa.portfoliohub.models.entities.Skill;
import com.gfa.portfoliohub.models.entities.SkillCategory;
import com.gfa.portfoliohub.repositories.SkillRepository;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SkillServiceImpl implements SkillService {

  private SkillRepository repository;
  private ModelMapper modelMapper;

  @Override
  public SkillResponseDTO findAllTheSkills() {
    return new SkillResponseDTO(repository.findAll().stream().map(s -> new SkillDTO(s)).collect(
        Collectors.toList()));
  }

  @Override
  public SkillCategoryResponseDto getAllTheSkillCategory() {
    return new SkillCategoryResponseDto(
        Arrays.stream(SkillCategory.values()).collect(Collectors.toList()));
  }

  @Override
  @Transactional
  public SkillDTO save(Skill request) {
    if (request.getId() != null) {
      Skill skillToSave = repository.findById(request.getId()).get();
      skillToSave.setName(request.getName());
      skillToSave.setSkillCategory(request.getSkillCategory());
      return modelMapper.map(repository.save(skillToSave), SkillDTO.class);
    }
    return modelMapper.map(repository.save(request), SkillDTO.class);
  }

  @Override
  public void deleteSkill(Integer id) throws IllegalArgumentException {
    if (!repository.existsById(id)) {
      throw new IllegalArgumentException("No such skill was found");
    }
    repository.deleteById(id);
  }

  @Override
  public Skill findById(Integer id) throws IllegalArgumentException {
    Skill skill = repository.findById(id).orElseThrow(IllegalArgumentException::new);
    return skill;
  }

}
