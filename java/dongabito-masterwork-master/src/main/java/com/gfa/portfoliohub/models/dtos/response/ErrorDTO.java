package com.gfa.portfoliohub.models.dtos.response;

import java.net.URI;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@ToString
public class ErrorDTO {
  private URI type;
  private String title;
  private HttpStatus status;
  private String detail;
  private Map<String, String> violations;
}
