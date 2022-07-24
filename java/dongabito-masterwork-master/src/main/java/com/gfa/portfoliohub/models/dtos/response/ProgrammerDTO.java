package com.gfa.portfoliohub.models.dtos.response;

import com.gfa.portfoliohub.models.entities.Programmer;
import java.time.Instant;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgrammerDTO extends ProgrammerCommonDTO {

  private String email;
  private Instant createdAt;
  private Instant lastLogin;

  public ProgrammerDTO(Programmer programmer) {
    super(programmer.getId(), programmer.getFirstName(), programmer.getLastName(),
        programmer.getCv(), programmer.getSkills().stream().map(SkillDTO::new).collect(
            Collectors.toList()));
    this.email = programmer.getEmail();
    this.createdAt = programmer.getCreatedAt();
    this.lastLogin = programmer.getLastLogin();
  }

}
