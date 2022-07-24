package com.gfa.portfoliohub.services;

import com.gfa.portfoliohub.exceptions.OperationBlockedByConstraintsException;
import com.gfa.portfoliohub.models.dtos.response.ProgrammerCommonDTO;
import com.gfa.portfoliohub.models.dtos.response.ProgrammerDTO;
import com.gfa.portfoliohub.models.dtos.response.ProgrammerResponseDTO;
import com.gfa.portfoliohub.models.entities.CurriculumVitae;
import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.models.entities.Skill;
import com.gfa.portfoliohub.models.entities.UserRole;
import com.gfa.portfoliohub.repositories.ProgrammerRepository;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProgrammerServiceImpl
    implements ProgrammerService, UserDetailsService {

  private ProgrammerRepository repository;
  private SkillService skillService;
  private ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;


  @Override
  public ProgrammerCommonDTO getProgrammerByID(Integer id) throws IllegalArgumentException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth.getPrincipal().equals("anonymousUser")) {
      return modelMapper.map(repository.findById(id)
              .orElseThrow(() -> new IllegalArgumentException("No such User")),
          ProgrammerCommonDTO.class);
    }
    ;
    return modelMapper.map(repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("No such User")), ProgrammerDTO.class);
  }

  @Override
  public ProgrammerResponseDTO getProgrammerBySkillID(Integer id) throws IllegalArgumentException {
    List<Programmer> programmers = repository.findProgrammerBySkillId(id);
    if (programmers.isEmpty()) {
      throw new IllegalArgumentException("Not an existing Skill ID");
    }
    return new ProgrammerResponseDTO(programmers.stream()
        .map(p -> modelMapper.map(p, ProgrammerCommonDTO.class))
        .collect(Collectors.toList()));
  }

  @Override
  public ProgrammerDTO saveProgrammer(Programmer programmer) {
    programmer.setPassword(passwordEncoder.encode(programmer.getPassword()));
    programmer.setRole(UserRole.ROLE_USER);
    return modelMapper.map(repository.save(programmer), ProgrammerDTO.class);
  }

  @Override
  public void deleteProgrammer(Integer id) {
    repository.deleteById(id);
  }

  @Override
  public CurriculumVitae addCVToPortfolio(Programmer programmer, CurriculumVitae cv) {
    if (programmer.getCv() != null) {
      cv.setId(programmer.getCv().getId());
    }
    programmer.setCv(cv);
    Programmer user = repository.save(programmer);
    return user.getCv();
  }

  @Override
  public boolean existsProgrammer(Programmer programmer) {
    return repository.existsById(programmer.getId());
  }

  @Override
  public ProgrammerDTO addSkillToPortfolio(Programmer programmer, Integer id) {
    programmer.addSkillToList(skillService.findById(id));
    repository.flush();
    return new ProgrammerDTO(programmer);
  }

  @Override
  public void removeSkillFromPortfolio(Programmer programmer, Integer id) {
    if (checkIfBelongsToRoadmap(programmer, id)) {
      throw new OperationBlockedByConstraintsException("First remove Roadmaps having this skill");
    }
    programmer.getSkills().removeIf(s -> Objects.equals(s.getId(), id));
    repository.save(programmer);
  }

  public boolean checkIfBelongsToRoadmap(Programmer programmer, Integer id)
      throws IllegalArgumentException {
    Skill skill = skillService.findById(id);
    return programmer.getRoadmaps().stream().anyMatch(r -> r.getSkills().contains(skill));
  }

  @Override
  public ProgrammerResponseDTO getProgrammerByTechnologyID(Integer id)
      throws IllegalArgumentException {
    List<Programmer> programmers = repository.findProgrammerByTechnologyID(id);
    if (programmers.isEmpty()) {
      throw new IllegalArgumentException("Not an existing Technology ID");
    }
    return new ProgrammerResponseDTO(programmers.stream()
        .map(p -> modelMapper.map(p, ProgrammerCommonDTO.class))
        .collect(Collectors.toList()));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Programmer user = repository.findProgrammerByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("No such user"));
    user.setLastLogin(Instant.now());
    repository.save(user);
    return user;
  }
}
