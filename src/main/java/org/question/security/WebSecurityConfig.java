package org.question.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

  private final CustomAuthenticationEntryPoint authenticationEntryPoint;

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
      final CustomUserDetailsService customUserDetailsService,
      final CustomAuthenticationEntryPoint customAuthenticationEntryPoint
//      final JwtRequestFilter jwtRequestFilter
  ) {
    super();
    this.customUserDetailsService = customUserDetailsService;
    this.authenticationEntryPoint = customAuthenticationEntryPoint;
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

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    log.trace("Configuring WebSecurityConfig");

    // The idea here is that the menu is accessible under /menuItem, which does not require authentication, so 
    // potential customers may always look at a menu. Customer operations like placing an order require a 
    // authentication with the CUSTOMER role. Administrative work, such as changing the menu, requires
    // authentication with the ADMIN role.
    //noinspection HardcodedFileSeparator
    http
        .csrf()
          .disable()
        .cors()
          .disable()
        .formLogin()
          .disable()
        .exceptionHandling()
          .authenticationEntryPoint(authenticationEntryPoint)
        .and()
        .authorizeRequests()
          .antMatchers("/admin/**")
            .hasRole("CUSTOMER")
          .antMatchers(
            "/login",
            "/view",
            "/**",
            "/home",
            "/swagger-ui.html",
            "/api-docs",
            "/configuration/**",
            "/swagger*/**",
            "/webjars/**",
            "/swagger-resources/**"
        )
          .permitAll()
        .anyRequest()
          .authenticated()
        .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//          .httpBasic()
        .and()
          .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class)
    ;
  }

  @Bean
  @Override
  protected UserDetailsService userDetailsService() {
    return customUserDetailsService;
  }
}
