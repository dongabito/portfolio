package com.gfa.portfoliohub.repositories;

import com.gfa.portfoliohub.models.entities.Programmer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProgrammerRepository extends JpaRepository<Programmer, Integer> {

  @Query(value = "SELECT p FROM programmers p JOIN  p.skills as s WHERE s.id = ?1")
  List<Programmer> findProgrammerBySkillId(Integer id);

  @Query(value = "SELECT p FROM programmers p JOIN  p.skills as s JOIN s.technologies as t WHERE t.name = ?1")
  List<Programmer> findProgrammerByTechnologyName(String name);

  Optional<Programmer> findProgrammerByEmail(String username);

  @Query(value = "SELECT p FROM programmers p JOIN  p.skills as s JOIN s.technologies as t WHERE t.id = ?1")
  List<Programmer> findProgrammerByTechnologyID(Integer id);

}
