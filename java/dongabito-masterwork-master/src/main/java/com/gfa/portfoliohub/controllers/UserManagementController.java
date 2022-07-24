package com.gfa.portfoliohub.controllers;

import com.gfa.portfoliohub.models.dtos.response.ProgrammerDTO;
import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.services.ProgrammerService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class UserManagementController {

  private ProgrammerService services;

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody Programmer programmer) {
    ProgrammerDTO newUser = services.saveProgrammer(programmer);
    log.info("New user created with email {}", programmer.getEmail());
    return ResponseEntity.status(201).body(newUser);
  }

  @DeleteMapping("/portfolio/{id}")
  public ResponseEntity<?> deleteProgrammer(@PathVariable Integer id) {
    services.deleteProgrammer(id);
    log.info("User with {} id is deleted", id);
    return ResponseEntity.noContent().build();
  }
}
