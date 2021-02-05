package org.question.security;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/4/21
 * <p>Time: 11:07 PM
 *
 * @author Miguel Mu\u00f1oz
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
  private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

  private static final long serialVersionUID = -7858869558953243875L;

  public CustomAuthenticationEntryPoint() {
    log.debug("Instantiating JwtAuthenticationEntryPoint");
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       final AuthenticationException authException) throws IOException {
    if (log.isDebugEnabled()) {
      log.debug("Error processing {}", request.getRequestURI());
    }
    if (authException instanceof CredentialsExpiredException) {
      response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, authException.getLocalizedMessage());
    }

    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getLocalizedMessage());
  }
}