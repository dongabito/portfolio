package com.gfa.portfoliohub.controllers;

import com.gfa.portfoliohub.models.dtos.response.SkillDTO;
import com.gfa.portfoliohub.models.entities.Skill;
import com.gfa.portfoliohub.models.entities.SkillCategory;
import com.gfa.portfoliohub.services.SkillService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/skill")
public class SkillController {

  private SkillService service;

  @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
  @GetMapping
  public ResponseEntity<?> getSkills() {
    return ResponseEntity.ok(service.findAllTheSkills());
  }

  @PostMapping
  public ResponseEntity<?> addSkillToList(@Valid @RequestBody Skill skill) {
    SkillDTO skillToAdd = service.save(skill);
    log.info("{} skill was added to the List", skillToAdd.getName());
    return ResponseEntity.status(201).body(skillToAdd);
  }

  @PutMapping
  public ResponseEntity<?> updateSkillToList(@Valid @RequestBody Skill skill) {
    SkillDTO skillToAdd = service.save(skill);
    log.info("{} skill was updated", skillToAdd.getName());
    return ResponseEntity.ok(skillToAdd);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteSkillFromList(@PathVariable Integer id) {
    service.deleteSkill(id);
    log.info("Skill with id Nr. {} was deleted", id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/skillcategory")
  public ResponseEntity getAllTheSkillCategory() {
    return ResponseEntity.ok(new ArrayList<>(Arrays.stream(SkillCategory.values()).collect(
        Collectors.toList())));
  }
}
