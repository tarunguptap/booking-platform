package com.organization.exception.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.organization.exception.BadRequestException;
import com.organization.exception.EntityAlreadyExistsException;
import com.organization.exception.ErrorResponse;
import com.organization.exception.InternalException;
import com.organization.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {
  private static final String REQUESTED_RESOURCE_NOT_FOUND = "Requested resource not found ";
  public static final String ENTITY_ALREADY_EXISTS = "Entity already exists";
  private static final String USER_NOT_AUTHORIZED = "User not authorized ";
  private static final String RECORD_NOT_FOUND = "Record Not Found ";
  private static final String BAD_REQUEST = "Bad Request ";

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exc) {
    String logMessage = REQUESTED_RESOURCE_NOT_FOUND;
    if (StringUtils.isNotBlank(exc.getLogMessage())) {
      logMessage = exc.getLogMessage();
    }
    logger.warn(logMessage, exc);
    ErrorResponse error = new ErrorResponse(REQUESTED_RESOURCE_NOT_FOUND, Arrays.asList(exc.getLocalizedMessage()));
    return new ResponseEntity(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
    String logMessage = ENTITY_ALREADY_EXISTS;
    if (StringUtils.isNotBlank(ex.getLogMessage())) {
      logMessage = ex.getLogMessage();
    }
    logger.error(logMessage, ex);
    ErrorResponse error = new ErrorResponse(ENTITY_ALREADY_EXISTS, Arrays.asList(ex.getLocalizedMessage()));
    return new ResponseEntity(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleValidationException(ValidationException exc) {
    logger.warn(BAD_REQUEST, exc);
    List<String> details = new ArrayList<>();
    details.add(exc.getLocalizedMessage());
    ErrorResponse error = new ErrorResponse(BAD_REQUEST, details);
    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleInternalException(InternalException exc) {
    logger.error("Error Occurred while performing action : ", exc);
    List<String> details = new ArrayList<>();
    details.add(exc.getLocalizedMessage());
    ErrorResponse error = new ErrorResponse("Internal Error", details);
    return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleDatabaseExceptions(DataIntegrityViolationException ex) {
    String errorMsg = "Database error while performing the action";
    if (ex.getRootCause() != null && StringUtils.isNotBlank(ex.getRootCause().getMessage())) {
      errorMsg = StringUtils.substringBefore(ex.getRootCause().getMessage(), ":");
    }
    logger.error(errorMsg, ex);
    ErrorResponse error = new ErrorResponse("Database Error", Arrays.asList(errorMsg));
    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleDatabaseExceptions(EmptyResultDataAccessException ex) {
    logger.error(REQUESTED_RESOURCE_NOT_FOUND, ex);
    ErrorResponse error = new ErrorResponse(RECORD_NOT_FOUND, Arrays.asList(REQUESTED_RESOURCE_NOT_FOUND));
    return new ResponseEntity(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException ex) {
    logger.error("Error while processing the request ", ex);
    ErrorResponse error = new ErrorResponse(BAD_REQUEST, Arrays.asList(ex.getMessage()));
    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    logger.error("Error while processing the request ", ex);
    ErrorResponse error = new ErrorResponse(BAD_REQUEST, Arrays.asList(ex.getMessage()));
    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
    logger.error("Error Occurred while performing action : ", ex);
    List<String> details = Arrays.asList("There is an error while processing your request, please contact administrator");
    ErrorResponse error = new ErrorResponse("Server Error", details);
    return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    List<String> details = new ArrayList<>();
    for (ObjectError error : ex.getBindingResult().getAllErrors()) {
      details.add(error.getDefaultMessage());
    }
    ErrorResponse error = new ErrorResponse("Validation Failed", details);
    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
    logger.error("Error Occurred while performing action : ", ex);
    List<String> details = new ArrayList<>();
    details.add(getErrorCodeAndMessage(ex.getLocalizedMessage()));
    ErrorResponse error = new ErrorResponse("Bad Request", details);
    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
  }

  private String getErrorCodeAndMessage(String json) {
    try {
      List<String> errorList = new ArrayList<>();
      LinkedHashMap errorsMap = new ObjectMapper().readValue(json, LinkedHashMap.class);
      ArrayList<LinkedHashMap> errors = (ArrayList<LinkedHashMap>) errorsMap.get("errors");
      errors.forEach(
          err -> {
            Map errorMap = new LinkedHashMap(2);
            errorMap.put("code", err.get("code"));
            errorMap.put("message", err.get("error"));
            errorList.add(errorMap.toString());
          });
      return errorList.toString();
    } catch (Exception e) {
      return json.replaceAll("[\\n\\t\" ]", " ");
    }
  }
}
