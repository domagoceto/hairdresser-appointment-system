package org.appointment.backend.service.impl;

import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.repo.KullaniciRepository;
import org.appointment.backend.service.MusteriDetailService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class MusteriDetailServiceImpl implements MusteriDetailService {
    private final KullaniciRepository kullaniciRepository;
    private final PasswordEncoder passwordEncoder;

    public MusteriDetailServiceImpl(KullaniciRepository kullaniciRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.kullaniciRepository = kullaniciRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("LoadUserByUsername çağrıldı. Kullanıcı email: {}", email);

        Kullanici kullanici = kullaniciRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Kullanıcı bulunamadı: {}", email);
                    return new UsernameNotFoundException("Kullanıcı bulunamadı: " + email);
                });

        // Rol kontrolü
        if (kullanici.getRol() == null) {
            log.error("Kullanıcının rolü atanmış değil: {}", kullanici.getEmail());
            throw new RuntimeException("Kullanıcının rolü atanmış değil!");
        }

        log.info("Kullanıcı bulundu: {}", kullanici);

        return new User(
                kullanici.getEmail(),
                kullanici.getSifre(),
                getAuthorities(kullanici)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Kullanici kullanici) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + kullanici.getRol().name()));
    }
}
