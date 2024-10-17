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
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 1. CSRF와 기본 인증 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 2. 세션 관리 설정 (Stateless)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                // 3. 경로별 권한 설정
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/api/user").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/example").permitAll()
                        .requestMatchers("/api/mail/**").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/playlist/**").permitAll()
                        .anyRequest().authenticated()
                )

                // 4. OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/oauth2/callback/google", true)
                );

        // 5. 필터 등록 (CORS → JWT → 예외 처리 순서)
        http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
        http.addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
    // CORS 설정 정의
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);  // 쿠키 및 인증 정보 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}