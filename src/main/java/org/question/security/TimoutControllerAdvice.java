package org.question.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This class gets instantiated but never called.
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/6/21
 * <p>Time: 6:28 PM
 *
 * @author Miguel Mu\u00f1oz
 */
@ControllerAdvice
public class TimoutControllerAdvice {

  public TimoutControllerAdvice() {
    log.debug("CustomControllerAdvice instantiated");
  }

  private static final Logger log = LoggerFactory.getLogger(TimoutControllerAdvice.class);
  @ResponseBody
  @ExceptionHandler(ExpectationFailed417Exception.class)
  public ResponseEntity<String> genericException(ExpectationFailed417Exception ex) {
    log.debug("CustomControllerAdvice activated");
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.EXPECTATION_FAILED);
  }
}