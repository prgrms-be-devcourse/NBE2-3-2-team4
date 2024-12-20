package com.team4.ttukttak_parking.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.ttukttak_parking.security.handler.JWTAccessDeniedHandler;
import com.team4.ttukttak_parking.security.handler.JWTAuthenticationEntryPoint;
import com.team4.ttukttak_parking.security.handler.JWTFilter;
import com.team4.ttukttak_parking.security.handler.JwtFilterExceptionHandler;
import com.team4.ttukttak_parking.security.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JWTAccessDeniedHandler jwtAccessDeniedHandler;
    private final TokenProvider tokenProvider;
    private static final String[] ANONYMOUS_MATCHERS = {
        "/api/auth/signup", "/api/auth/login",
        "/api/auth/reissue", "/error", "/api/json"
    };

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
        throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
            .headers((headerConfig) ->
                headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
            )
            .authorizeHttpRequests((authorizeRequests) ->
                authorizeRequests
                    .requestMatchers(ANONYMOUS_MATCHERS).permitAll()
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            .sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(new JWTFilter(tokenProvider),
                UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtFilterExceptionHandler(new ObjectMapper()), JWTFilter.class)
            .exceptionHandling((exceptionHandling) ->
                exceptionHandling
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler)
            )
            .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers(new AntPathRequestMatcher("/favicon.ico"))
            .requestMatchers(new AntPathRequestMatcher("/css/**"))
            .requestMatchers(new AntPathRequestMatcher("/js/**"))
            .requestMatchers(new AntPathRequestMatcher("/image/**"))
            .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**"))
            .requestMatchers(new AntPathRequestMatcher("/test"))
            .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**"))
            .requestMatchers(new AntPathRequestMatcher("/swagger-ui.html"));

    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
