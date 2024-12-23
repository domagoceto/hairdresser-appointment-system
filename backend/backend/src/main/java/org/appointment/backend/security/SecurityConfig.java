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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

        ProviderManager providerManager = new ProviderManager(List.of(musteriProvider, kuaforProvider));

        // Test amaçlı loglama
        providerManager.getProviders().forEach(provider -> System.out.println("Provider: " + provider.getClass().getName()));

        return providerManager;
    }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF'yi devre dışı bırak
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS yapılandırması
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        .requestMatchers("/kuafor/register", "/kuafor/login").permitAll()
                        .requestMatchers("/kuafor/tumKuaforler").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/hizmetler/ekle").hasRole("ADMIN")
                        .requestMatchers("/api/kuaforler/**").hasRole("KUAFOR")
                        .requestMatchers("/admin/me").hasRole("ADMIN")
                        .requestMatchers("/user/me").hasRole("MUSTERI")
                        .requestMatchers("/kuafor/all").hasRole("MUSTERI")
                        .requestMatchers("/kuafor/{kuaforId}/hizmetler/kullanici").hasRole("MUSTERI")
                        .requestMatchers("/kuafor/**").hasAnyRole("KUAFOR", "ADMIN","MUSTERI")
                        .requestMatchers("/kuafor/me").hasRole("KUAFOR") // Sadece 'KUAFOR' rolü
                        .requestMatchers("/user/**").hasRole("MUSTERI")
                        .requestMatchers("/hizmetler", "/randevu/kuaforler").hasRole("MUSTERI") // Müşteri erişimi
                        .requestMatchers("/favicon.ico", "/logo192.png", "/error").permitAll()
                        .requestMatchers("/odeme/yontemler", "/odeme/durumlar").hasRole("ADMIN") // Admin yetkisi gerekiyor
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // JWT doğrulama filtresi
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Stateless oturum
        return http.build();
    }






    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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

