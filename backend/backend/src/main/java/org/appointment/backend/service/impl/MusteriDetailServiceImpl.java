package org.appointment.backend.service.impl;

import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.repo.KullaniciRepository;
import org.appointment.backend.service.MusteriDetailService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MusteriDetailServiceImpl implements MusteriDetailService, UserDetailsService {
    private final KullaniciRepository kullaniciRepository;
    private final PasswordEncoder passwordEncoder;

    public MusteriDetailServiceImpl(KullaniciRepository kullaniciRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.kullaniciRepository = kullaniciRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Kullanici kullanici = kullaniciRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + email));

        return User.builder()
                .username(kullanici.getEmail())
                .password(kullanici.getSifre())
                .roles(kullanici.getRol().name()) // Enum rolünü string olarak kullanıyoruz
                .build();
    }

}



