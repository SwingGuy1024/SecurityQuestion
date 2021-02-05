package org.question.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.question.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/4/21
 * <p>Time: 11:09 PM
 *
 * @author Miguel Mu\u00f1oz
 */
public class RequestFilter extends OncePerRequestFilter {
  private static final Logger log = LoggerFactory.getLogger(RequestFilter.class);

  private Supplier<SecurityContext> contextSupplier = SecurityContextHolder::getContext;

  public RequestFilter() {
    super();
    log.trace("Instantiating JwtRequestFilter");
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain
  ) throws ServletException, IOException {
    log.trace("JwtRequestFilter.doFilterInternal");

    processFilter(request);
    log.trace("filter processed");
    chain.doFilter(request, response);
    log.trace("Filter chained");
  }

  private void processFilter(final HttpServletRequest request) {
    String uri = request.getRequestURI();
    String role = "CUSTOMER";
    log.debug("URI is {}", uri);
    if (uri.contains("One")) {
      throw new CredentialsExpiredException("Method One");
    }
    if (uri.contains("Two")) {
      throw new ExpectationFailed417Exception("Method Two");
    }
    if (uri.contains("Five")) {
      role = "UNKNOWN";
    }

    // Once we get the token validate it.
    final Collection<? extends GrantedAuthority> authorities = Collections.singleton(new User.Authority(role));
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
        = new UsernamePasswordAuthenticationToken("Dummy", null, authorities);
    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    // After setting the Authentication in the context, we specify that the current user is authenticated.
    // So it passes the Spring Security Configurations successfully.
    final SecurityContext context = contextSupplier.get();
    context.setAuthentication(usernamePasswordAuthenticationToken);
  }
}
