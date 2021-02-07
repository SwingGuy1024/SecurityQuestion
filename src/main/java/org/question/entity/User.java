package org.question.entity;

import java.util.Collection;
import java.util.Collections;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/4/21
 * <p>Time: 10:45 PM
 *
 * @author Miguel Mu\u00f1oz
 */
@Entity
public class User implements UserDetails {
  @Id
  @Column(unique = true)
  private String username = null;

  private String password = null;

  public User() {}
  
  public User(String username) { this.username = username; }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new Authority("ADMIN"));
  }

  public static final class Authority implements GrantedAuthority {
    private final String authority;

    public Authority(String role) {
      //noinspection HardCodedStringLiteral
      authority = String.format("ROLE_%s", role);
    }

    @Override
    public String getAuthority() {
      return authority;
    }

    @Override
    public String toString() {
      return authority;
    }
  }
}
