package org.cosmic.backend.globals.configs;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.globals.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Log4j2
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter myJwtAuthenticationFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS
        ))
        .addFilterBefore(myJwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(authorize ->
            authorize
                .requestMatchers("/api/auth/**", "/api/mail/**", "/api/user").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/oauth2/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/file/").authenticated()
                .requestMatchers("/api/file/**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll())
        .exceptionHandling(exceptionHAndlerFilter -> {
          exceptionHAndlerFilter
              .authenticationEntryPoint((request, response, authException) -> response.sendError(
                  HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다."));
        })
        .oauth2Login(oauth2 -> oauth2
            .defaultSuccessUrl("/oauth2/callback/google", true)
        )
    ;
    return http.build();
  }
}