package com.gfa.portfoliohub.models.dtos.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgrammerResponseDTO {
  private List<ProgrammerCommonDTO> programmers = new ArrayList<>();
}
