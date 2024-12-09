package org.appointment.backend.security;

import org.appointment.backend.service.MusteriDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final MusteriDetailService musteriDetailService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(MusteriDetailService musteriDetailService, JwtUtil jwtUtil) {
        this.musteriDetailService = musteriDetailService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF korumasını devre dışı bırak
                .authorizeHttpRequests(auth -> auth
                        // Register ve Login endpoint'leri herkese açık
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        // Sadece ADMIN rolüne sahip kullanıcılar için
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Sadece MUSTERI rolüne sahip kullanıcılar için
                        .requestMatchers("/user/**").hasRole("MUSTERI")
                        // Diğer tüm endpoint'ler için kimlik doğrulaması gerekli
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class) // JWT doğrulama filtresi
                .cors(cors -> cors.disable()); // CORS yapılandırmasını devre dışı bırak
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(musteriDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
