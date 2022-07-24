package com.gfa.portfoliohub.services;

import com.gfa.portfoliohub.models.dtos.response.TechnologyDTO;
import com.gfa.portfoliohub.models.dtos.response.TechnologyResponseDTO;
import com.gfa.portfoliohub.models.entities.Skill;
import com.gfa.portfoliohub.models.entities.Technology;
import com.gfa.portfoliohub.repositories.TechnologyRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TechnologyServiceImpl
    implements TechnologyService {

  private TechnologyRepository repository;
  private ModelMapper modelMapper;
  private SkillService skillService;

  @Override
  public Technology getTechnologyById(Integer id) throws IllegalArgumentException {
    return repository.findById(id).orElseThrow(IllegalArgumentException::new);
  }

  @Override
  public TechnologyResponseDTO findAllTheTechnologies() {
    return new TechnologyResponseDTO(StreamSupport.stream(repository.findAll().spliterator(), false)
        .map(t -> modelMapper.map(t, TechnologyDTO.class)).collect(Collectors.toList()));
  }

  @Override
  public TechnologyDTO addTechnology(Technology technology) {
    return modelMapper.map(repository.save(technology), TechnologyDTO.class);
  }

  @Override
  public void deleteTechnology(Integer technologyId) {
    if (!repository.existsById(technologyId)) {
      throw new IllegalArgumentException("No such technology");
    }
    repository.deleteById(technologyId);
  }

  @Override
  public List<String> listTheKeywords() {
    return repository.listAllTheKeywords();
  }

  @Override
  public TechnologyDTO addTechToSkill(Integer skillId, Integer id) throws IllegalArgumentException {
    Technology technology = repository.findById(id).orElseThrow(IllegalArgumentException::new);
    Skill skill = skillService.findById(skillId);
    technology.getSkills().add(skill);
    return modelMapper.map(repository.save(technology), TechnologyDTO.class);
  }

  @Override
  public void deleteTechFromSkill(Integer skillId, Integer id) throws IllegalArgumentException {
    Technology technology = repository.findById(id).orElseThrow(IllegalArgumentException::new);
    Skill skill = skillService.findById(skillId);
    technology.getSkills().remove(skill);
    repository.save(technology);
  }
}
