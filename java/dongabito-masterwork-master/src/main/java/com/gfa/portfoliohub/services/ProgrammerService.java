package com.gfa.portfoliohub.services;

import com.gfa.portfoliohub.models.dtos.response.ProgrammerCommonDTO;
import com.gfa.portfoliohub.models.dtos.response.ProgrammerDTO;
import com.gfa.portfoliohub.models.dtos.response.ProgrammerResponseDTO;
import com.gfa.portfoliohub.models.entities.CurriculumVitae;
import com.gfa.portfoliohub.models.entities.Programmer;

public interface ProgrammerService {

  ProgrammerCommonDTO getProgrammerByID(Integer id) throws IllegalArgumentException;

  ProgrammerResponseDTO getProgrammerBySkillID(Integer id) throws IllegalArgumentException;

  ProgrammerDTO saveProgrammer(Programmer programmer);

  void deleteProgrammer(Integer id);

  CurriculumVitae addCVToPortfolio(Programmer programmer, CurriculumVitae cv);

  boolean existsProgrammer(Programmer programmer);

  ProgrammerDTO addSkillToPortfolio(Programmer principal, Integer id);

  void removeSkillFromPortfolio(Programmer principal, Integer id);

  ProgrammerResponseDTO getProgrammerByTechnologyID(Integer id) throws IllegalArgumentException;
}
