package org.question.security;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/4/21
 * <p>Time: 10:41 PM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

  private final CustomUserDetailsService customUserDetailsService;

  private final RequestFilter requestFilter;

  private final PasswordEncoder encoder = new BCryptPasswordEncoder();

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder authBuilder) {
    authBuilder.eraseCredentials(false);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return encoder;
  }

  @Autowired
  public WebSecurityConfig(
      final CustomUserDetailsService customUserDetailsService
  ) {
    super();
    this.customUserDetailsService = customUserDetailsService;
    requestFilter = new RequestFilter();
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
  }

  @SuppressWarnings({"HardCodedStringLiteral", "HardcodedFileSeparator"})
  @Override
  public void configure(WebSecurity web) {
    web.ignoring().mvcMatchers(HttpMethod.OPTIONS, "/**");
    // ignore swagger 
    web
        .ignoring()
        .mvcMatchers("/swagger-ui.html/**", "/configuration/**", "/swagger-resources/**", "/v2/api-docs", "/webjars/**")
    ;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

    @SuppressWarnings("HardcodedFileSeparator")
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
      log.trace("Configuring WebSecurityConfig");
  
      /* The /view/** path requires no authentication. The others start with /admin, and require the ADMIN role. */
      http
          .csrf()
            .disable()
          .cors()
            .disable()
          .formLogin()
            .disable()
          .authorizeRequests()
            .antMatchers("/admin/**")
              .hasRole("ADMIN")
            .antMatchers("/view/**")
              .permitAll()
          .anyRequest()
            .authenticated()
          .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
            .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class)
          .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
      ;
    }

  @Bean
  @Override
  protected UserDetailsService userDetailsService() {
    return customUserDetailsService;
  }
  
  private final CustomAuthenticationEntryPoint authenticationEntryPoint = new CustomAuthenticationEntryPoint();
  
  static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -7858869558953243875L;

    CustomAuthenticationEntryPoint() {
      log.debug("Instantiating JwtAuthenticationEntryPoint");
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
      log.debug("Auth exception of {}", authException.getClass());
      
      if (authException instanceof CredentialsExpiredException) {
        response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, "Expired Token"); // This never gets executed
      }
      response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden for unknown reason"); // The message goes nowhere.
    }
  }
}
