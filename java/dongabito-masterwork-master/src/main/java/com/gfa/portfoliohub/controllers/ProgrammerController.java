package com.gfa.portfoliohub.controllers;

import com.gfa.portfoliohub.models.dtos.request.ProgressRequestDTO;
import com.gfa.portfoliohub.models.dtos.response.ProgrammerDTO;
import com.gfa.portfoliohub.models.dtos.response.RoadmapDTO;
import com.gfa.portfoliohub.models.entities.CurriculumVitae;
import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.services.ProgrammerService;
import com.gfa.portfoliohub.services.ProgressInTechnologyService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/portfolio")
public class ProgrammerController {

  private ProgrammerService services;
  private ProgressInTechnologyService progressService;

  @GetMapping
  public ResponseEntity<?> getPortfolio(Authentication authentication) {
    Programmer programmer = (Programmer) authentication.getPrincipal();
    return ResponseEntity.ok(services.getProgrammerByID(programmer.getId()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getPortfolio(@PathVariable Integer id) {
    return ResponseEntity.ok(services.getProgrammerByID(id));
  }

  @GetMapping("/skill/{id}")
  //TODO:rövidebb DTO a programozókról
  public ResponseEntity<?> getProgrammersBySkillId(@PathVariable Integer id) {
    return ResponseEntity.ok(services.getProgrammerBySkillID(id));
  }

  @PostMapping("/cv")
  public ResponseEntity<?> addCVToPortfolio(Authentication authentication, @Valid @RequestBody
      CurriculumVitae cv) {
    CurriculumVitae curriculumVitae =
        services.addCVToPortfolio((Programmer) authentication.getPrincipal(), cv);
    log.info("New CV was added to user: {}",
        ((Programmer) authentication.getPrincipal()).getEmail());
    return ResponseEntity.status(201).body(curriculumVitae);
  }

  @PutMapping("/cv")
  public ResponseEntity<?> updateCVToPortfolio(Authentication authentication, @Valid @RequestBody
      CurriculumVitae cv) {
    CurriculumVitae curriculumVitae =
        services.addCVToPortfolio(((Programmer) authentication.getPrincipal()), cv);
    log.info("CV was updated from user: {}",
        ((Programmer) authentication.getPrincipal()).getEmail());
    return ResponseEntity.ok(curriculumVitae);
  }

  @PostMapping("/skill/{id}")
  public ResponseEntity<?> addSkillToPortfolio(Authentication authentication,
                                               @PathVariable Integer id) {
    ProgrammerDTO programmer =
        services.addSkillToPortfolio((Programmer) authentication.getPrincipal(), id);
    log.info("New skill with id Nr. {} was added to user: {}", id,
        ((Programmer) authentication.getPrincipal()).getEmail());
    return ResponseEntity.ok(programmer);
  }

  @DeleteMapping("/skill/{id}")
  public ResponseEntity<?> removeSkillToPortfolio(Authentication authentication,
                                                  @PathVariable Integer id) {
    services.removeSkillFromPortfolio((Programmer) authentication.getPrincipal(), id);
    log.info("Skill with id Nr. {} was deleted from user: {}", id,
        ((Programmer) authentication.getPrincipal()).getEmail());
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/technology/{id}")
  public ResponseEntity<?> getProgrammersByTechnologyId(@PathVariable Integer id) {
    return ResponseEntity.ok(services.getProgrammerByTechnologyID(id));
  }

  @PostMapping("/technology/progress")
  public ResponseEntity<?> makeProgress(Authentication authentication, @Valid @RequestBody
      ProgressRequestDTO progress) {
    RoadmapDTO roadmap = progressService.makeProgress(authentication, progress);
    log.info("Progress was added to Roadmap Nr. {}", roadmap.getId());
    return ResponseEntity.status(201).body(roadmap);
  }

  @PutMapping("/technology/progress/{id}")
  public ResponseEntity<?> updateProgress(Authentication authentication, @Valid @RequestBody
      ProgressRequestDTO progress, @PathVariable Integer id) {
    RoadmapDTO roadmap = progressService.updateProgress(authentication, progress, id);
    log.info("Progress was updated to Roadmap Nr. {}", roadmap.getId());
    return ResponseEntity.ok(roadmap);
  }

  @DeleteMapping("/technology/progress/{id}")
  public ResponseEntity<?> deleteProgress(Authentication authentication, @PathVariable Integer id) {
    progressService.deleteProgress(authentication, id);
    log.info("Progress Nr. {} was deleted", id);
    return ResponseEntity.noContent().build();
  }

}
