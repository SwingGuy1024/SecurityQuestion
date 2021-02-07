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
import org.springframework.security.web.authentication.WebAuthenticationDetails;
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

  private final Supplier<SecurityContext> contextSupplier = SecurityContextHolder::getContext;

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

  // In the real application, the JWT token gets parsed and processed here, so this is where the application determines if a JwtToken is
  // authentic and isn't expired. It doesn't look at the role, which is handled by Spring Security outside of the application code.
  private void processFilter(final HttpServletRequest request) {
    String uri = request.getRequestURI();
    String role = "ADMIN";
    log.debug("URI is {}", uri);
    if (uri.contains("failureModeFour")) {
      log.debug("Setting a usernamePasswordAuthenticationToken with empty Authorization list");
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
          = new UsernamePasswordAuthenticationToken("Dummy", "expired credentials", Collections.emptyList());
      final WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails(request);
      usernamePasswordAuthenticationToken.setDetails(details);
      contextSupplier.get().setAuthentication(usernamePasswordAuthenticationToken);
      return;
    }
    if (uri.contains("failureModeOne")) {
      log.debug("Throwing CredentialsExpiredException");
      throw new CredentialsExpiredException("failureModeOne"); // Extends AuthenticationException
    }
    if (uri.contains("failureModeThree")) {
      log.debug("Throwing ExpectationFailed417Exception");
      throw new ExpectationFailed417Exception("failureModeThree");
    }
    if (uri.contains("methodThree")) {
      role = "UNKNOWN";
    }
    if (uri.contains("view")) {
      log.debug("Skipping Authentication.");
      // view requires no authentication. In a normal application, we bail because it doesn't have a bearer token. Here we just
      // bail because we know it needs no authentication.
      return;
    }

    log.debug("Processing request with role {}.", role);
    // Once we get the token validate it.
    final Collection<? extends GrantedAuthority> authorities = Collections.singleton(new User.Authority(role));
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
        = new UsernamePasswordAuthenticationToken("Dummy", "expired credentials", authorities);
    final WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails(request);
    
    usernamePasswordAuthenticationToken.setDetails(details);
    // After setting the Authentication in the context, we specify that the current user is authenticated.
    // So it passes the Spring Security Configurations successfully.
    final SecurityContext context = contextSupplier.get();
    context.setAuthentication(usernamePasswordAuthenticationToken);
  }
}
