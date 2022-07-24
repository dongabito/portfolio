package com.gfa.portfoliohub.exceptions;

import com.gfa.portfoliohub.models.dtos.response.ErrorDTO;
import java.net.URI;
import java.sql.SQLIntegrityConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex) {
    int numberOfErrors = ex.getBindingResult().getAllErrors().size();
    String errorCode = ex.getBindingResult().getFieldError().getCode();
    ErrorDTO errorDTO = ErrorDTO.builder()
        .type(URI.create("portfolio/invalid-parameter"))
        .title("Not found")
        .status(HttpStatus.NOT_FOUND)
        .detail(ex.getMessage())
        .build();
    return ResponseEntity.status(404).contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(errorDTO);
  }

  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
  public ResponseEntity handleSqlExceptions(SQLIntegrityConstraintViolationException ex) {
    ErrorDTO errorDTO = ErrorDTO.builder()
        .type(URI.create("/constraint-violation"))
        .title("SQL constraint violation")
        .status(HttpStatus.CONFLICT)
        .detail(ex.getMessage())
        .build();
    return ResponseEntity.status(409).contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(errorDTO);
  }

  @ExceptionHandler(HttpClientErrorException.MethodNotAllowed.class)
  //TODO:nem kapja el
  public ResponseEntity handleMNAExceptions(HttpClientErrorException.MethodNotAllowed ex) {
    ErrorDTO errorDTO = ErrorDTO.builder()
        .type(URI.create("/method-not-allowed"))
        .title("This method is not supported by the Server")
        .status(HttpStatus.METHOD_NOT_ALLOWED)
        .detail(ex.getMessage())
        .build();
    return ResponseEntity.status(400).contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(errorDTO);
  }

  @ExceptionHandler(NumberFormatException.class)
  public ResponseEntity handleNFE(NumberFormatException ex) {
    ErrorDTO errorDTO = ErrorDTO.builder()
        .type(URI.create("/parameter-is-not-a-number"))
        .title("Not an appropriate parameter")
        .status(HttpStatus.NOT_FOUND)
        .detail(ex.getMessage())
        .build();
    return ResponseEntity.status(404).contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(errorDTO);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity handleIAE(IllegalArgumentException ex) {
    ErrorDTO errorDTO = ErrorDTO.builder()
        .type(URI.create("/parameter-is-not-valid"))
        .title("Not an appropriate parameter")
        .status(HttpStatus.NOT_FOUND)
        .detail(ex.getMessage())
        .build();
    return ResponseEntity.status(404).contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(errorDTO);
  }

  @ExceptionHandler(OperationBlockedByConstraintsException.class)
  public ResponseEntity handleIAE(OperationBlockedByConstraintsException ex) {
    ErrorDTO errorDTO = ErrorDTO.builder()
        .type(URI.create("/operation-blocked-by-constraints"))
        .title("Operation depends on other objects")
        .status(HttpStatus.CONFLICT)
        .detail(ex.getMessage())
        .build();
    return ResponseEntity.status(409).contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(errorDTO);
  }
}
