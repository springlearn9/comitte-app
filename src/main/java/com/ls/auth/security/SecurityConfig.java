package com.ls.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF protection, common for stateless REST APIs
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Configure authorization rules
                .authorizeHttpRequests(authorize -> authorize
                        // Allow access to public endpoints without authentication
                        .requestMatchers("/swagger-ui/**", "/api-docs/**").permitAll()
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers("/api/password/**").permitAll()
                        .requestMatchers("/api/comittes/**", "/api/members/**").permitAll()
                        .requestMatchers("/api/bids/**", "/api/comitte-member-map/**").permitAll()
                        // Require ADMIN role for admin endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )

                // 3. Configure authentication mechanism (e.g., HTTP Basic)
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000","http://localhost:5173","http://localhost:5174", "http://localhost:5175") // frontend origin (use exact origin in prod)
                        .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}
