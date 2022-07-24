package com.gfa.portfoliohub.models.dtos.request;


import java.time.Instant;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProgressRequestDTO {

  @NotBlank
  private String learningProgress;
  private String details;
  @Past
  private Instant startedAt;
  @NotNull
  private Integer technology_id;
  @NotNull
  private Integer roadmap_id;

}
