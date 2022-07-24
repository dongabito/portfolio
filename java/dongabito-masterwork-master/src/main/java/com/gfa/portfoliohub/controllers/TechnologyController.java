package com.gfa.portfoliohub.controllers;

import com.gfa.portfoliohub.models.dtos.response.TechnologyDTO;
import com.gfa.portfoliohub.models.entities.Technology;
import com.gfa.portfoliohub.services.TechnologyService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/technology")
@AllArgsConstructor
@Slf4j
public class TechnologyController {

  private TechnologyService technologyService;

  @GetMapping
  public ResponseEntity<?> getAllTheTechnologies() {
    return ResponseEntity.ok(technologyService.findAllTheTechnologies());
  }

  @PostMapping
  public ResponseEntity<?> addTechnologyToList(@Valid @RequestBody Technology technology) {
    TechnologyDTO technologyToSave = technologyService.addTechnology(technology);
    log.info("{} saved by admin", technologyToSave.getName());
    return ResponseEntity.status(201).body(technologyToSave);
  }

  @PutMapping
  public ResponseEntity<?> updateTechnology(@Valid @RequestBody Technology technology) {
    TechnologyDTO technologyToUpdate = technologyService.addTechnology(technology);
    log.info("{} updated by admin", technologyToUpdate.getName());
    return ResponseEntity.ok(technologyToUpdate);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteTechnology(@PathVariable Integer id) {
    technologyService.deleteTechnology(id);
    log.info("Technology with id Nr. {} was deleted", id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/keywords")
  public ResponseEntity<?> listKeywordsOfTechnology() {
    return ResponseEntity.ok(technologyService.listTheKeywords());
  }

  @PostMapping("/{id}/skill/{skillId}")
  public ResponseEntity<?> addTechnologyToSkillList(@PathVariable Integer skillId,
                                                    @PathVariable Integer id) {
    TechnologyDTO techToReturn = technologyService.addTechToSkill(skillId, id);
    log.info("{} was added to list of skill with ID Nr. {}", techToReturn.getName(), skillId);
    return ResponseEntity.ok(techToReturn);
  }

  @DeleteMapping("/{id}/skill/{skillId}")
  public ResponseEntity<?> removeTechnologyFromSkillList(@PathVariable Integer skillId,
                                                         @PathVariable Integer id) {
    technologyService.deleteTechFromSkill(skillId, id);
    log.info("Technology with ID Nr. {} was deleted from list of skill with ID Nr. {}", id,
        skillId);
    return ResponseEntity.noContent().build();
  }
}
