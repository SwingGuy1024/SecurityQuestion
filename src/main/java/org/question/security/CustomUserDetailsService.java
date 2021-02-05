package org.question.security;

import org.question.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/4/21
 * <p>Time: 10:43 PM
 *
 * @author Miguel Mu\u00f1oz
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
  private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

  private final PasswordEncoder encoder = new BCryptPasswordEncoder();

  @Autowired
  public CustomUserDetailsService() {
    log.debug("Instantiating JwtUserDetailsService");
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    log.trace("username: {}", username);
    return new User(username);
  }

  public PasswordEncoder getEncoder() { return encoder; }
}
