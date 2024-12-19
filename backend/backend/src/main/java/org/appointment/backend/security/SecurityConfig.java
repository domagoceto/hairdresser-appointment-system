package org.appointment.backend.security;

import org.appointment.backend.service.KuaforDetailService;
import org.appointment.backend.service.MusteriDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final MusteriDetailService musteriDetailService;
    private final KuaforDetailService kuaforDetailService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(MusteriDetailService musteriDetailService, KuaforDetailService kuaforDetailService, JwtUtil jwtUtil) {
        this.musteriDetailService = musteriDetailService;
        this.kuaforDetailService = kuaforDetailService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        DaoAuthenticationProvider musteriProvider = musteriAuthenticationProvider();
        DaoAuthenticationProvider kuaforProvider = kuaforAuthenticationProvider();

        return new ProviderManager(List.of(musteriProvider, kuaforProvider));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF korumasını devre dışı bırak
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login").permitAll() // Genel erişim
                        .requestMatchers("/kuafor/register", "/kuafor/login").permitAll() // Kuaför login ve register erişimi
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Sadece admin erişimi
                        .requestMatchers("/hizmetler/ekle").hasRole("ADMIN")
                        .requestMatchers("/kuafor/**").hasRole("KUAFOR") // Sadece kuaför erişimi
                        .requestMatchers("/user/**").hasRole("MUSTERI") // Sadece müşteri erişimi
                        .anyRequest().authenticated() // Diğer tüm endpoint'ler için yetkilendirme
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.disable()); // CORS yapılandırmasını devre dışı bırak
        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider musteriAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(musteriDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public DaoAuthenticationProvider kuaforAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(kuaforDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}

