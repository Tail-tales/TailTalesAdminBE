package com.tailtales.backend.config;

import com.tailtales.backend.auth.service.CustomUserDetailsService;
import com.tailtales.backend.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder; // Import 추가
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Lazy
    @Autowired
    private JwtFilter jwtFilter;

    @Lazy
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder); // 주입받은 PasswordEncoder 사용
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable()) // CSRF 비활성화 (필요에 따라 설정)
                .cors((cors) -> cors
                        .configurationSource(request -> {
                            CorsConfiguration configuration = new CorsConfiguration();
                            configuration.setAllowedOrigins(List.of("*")); // 모든 Origin 허용 (운영 환경에서는 구체적인 Origin으로 설정해야 함)
                            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                            configuration.setAllowedHeaders(List.of("*"));
                            configuration.setAllowCredentials(true); // 쿠키 허용 설정 (필요한 경우)
                            return configuration;
                        })
                )
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login").permitAll() // 로그인 경로는 인증 없이 접근 허용
                        .requestMatchers("/api/admins/**").authenticated() // "/admin/**" 경로는 인증 필요
                        .anyRequest().permitAll() // 일단 모든 요청 허용 (추후 수정)
                )
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS) // 세션 사용 안 함
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터 추가
                .formLogin(form -> form.disable());
        return http.build();
    }

}