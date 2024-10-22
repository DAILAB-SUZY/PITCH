package org.cosmic.backend.globals.configs;

import org.cosmic.backend.domain.auth.applications.JwtAuthenticationFilter;
import org.cosmic.backend.globals.exceptions.ExceptionHandlerFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          ExceptionHandlerFilter exceptionHandlerFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.exceptionHandlerFilter = exceptionHandlerFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
            )).authorizeHttpRequests(authorize ->
                authorize
                .requestMatchers("/oauth2/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/user").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/example").permitAll()
                .requestMatchers("/api/mail/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()

            ).oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/oauth2/callback/google", true)
                );

        //filter등록 후 매 요청마다 CorsFilter 실행한 후에 jwtAuthenticationFilter 실행한다.
        http.addFilterAfter(
                jwtAuthenticationFilter,
                CorsFilter.class
        );
        http.addFilterBefore(
                exceptionHandlerFilter,
                JwtAuthenticationFilter.class
        );
         return http.build();
    }
}