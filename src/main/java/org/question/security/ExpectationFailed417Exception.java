package org.question.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/5/21
 * <p>Time: 2:34 AM
 *
 * @author Miguel Mu\u00f1oz
 */
@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class ExpectationFailed417Exception extends RuntimeException {
  public ExpectationFailed417Exception(final String message) {
    super(message);
  }
}
