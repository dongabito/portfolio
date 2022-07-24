package com.gfa.portfoliohub.controllers;

import com.gfa.portfoliohub.models.dtos.response.RoadmapDTO;
import com.gfa.portfoliohub.models.entities.Roadmap;
import com.gfa.portfoliohub.services.RoadmapService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roadmap")
@AllArgsConstructor
@Slf4j
public class RoadmapController {

  private RoadmapService service;

  @GetMapping
  public ResponseEntity<?> getAllRoadmaps(@RequestParam(name = "id", required = false) Integer id) {
    if (id == null || id < 0) {
      return ResponseEntity.ok(service.getAllTheMaps());
    } else {
      return ResponseEntity.ok(service.findMapByOwner(id));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getRoadmap(@PathVariable Integer id) {
    return ResponseEntity.ok(new RoadmapDTO(service.findById(id)));
  }

  @PostMapping
  public ResponseEntity<?> addRoadmap(@RequestBody Roadmap roadmap, Authentication authentication) {
    RoadmapDTO roadmapToReturn = service.saveRoadmap(roadmap, authentication);
    log.info("Roadmap with name: {} was added", roadmapToReturn.getName());
    return ResponseEntity.status(201).body(roadmapToReturn);
  }

  @PutMapping
  public ResponseEntity<?> updateRoadmap(@RequestBody Roadmap roadmap,
                                         Authentication authentication) {
    RoadmapDTO roadmapToReturn = service.saveRoadmap(roadmap, authentication);
    log.info("Roadmap with name: {} was updated", roadmapToReturn.getName());
    return ResponseEntity.ok(roadmapToReturn);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteRoadmap(@PathVariable Integer id, Authentication authentication) {
    service.deleteRoadmap(id, authentication);
    log.info("Roadmap with ID Nr. {} was deleted", id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/skill/{skillId}")
  public ResponseEntity<?> addSkillToRoadmap(@PathVariable Integer id,
                                             @PathVariable Integer skillId, Authentication auth) {
    return ResponseEntity.ok(service.addSkillToRoadmap(id, skillId, auth));
  }

  @DeleteMapping("/{id}/skill/{skillId}")
  public ResponseEntity<?> removeSkillFromRoadmap(@PathVariable Integer id,
                                                  @PathVariable Integer skillId,
                                                  Authentication auth) {
    return ResponseEntity.ok(service.removeSkillToRoadmap(id, skillId, auth));
  }

}
