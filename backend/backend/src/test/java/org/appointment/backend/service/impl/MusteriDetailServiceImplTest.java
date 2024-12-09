package org.appointment.backend.service.impl;

import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.entity.Rol;
import org.appointment.backend.repo.KullaniciRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MusteriDetailServiceImplTest {

    private final KullaniciRepository kullaniciRepository = mock(KullaniciRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final MusteriDetailServiceImpl musteriDetailService =
            new MusteriDetailServiceImpl(kullaniciRepository, passwordEncoder);


    @Test
    void testLoadUserByUsername_Success() {
        // Test için bir kullanıcı mock'u oluşturuyoruz
        Kullanici kullanici = new Kullanici();
        kullanici.setEmail("test@example.com");
        kullanici.setSifre("hashedpassword");
        kullanici.setRol(Rol.MUSTERI); // Kullanıcı rolü ayarlanıyor

        // Kullanıcı repository'sinden email ile kullanıcı bulunması simule ediliyor
        when(kullaniciRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(kullanici));

        // UserDetails'i yükleme
        UserDetails userDetails = musteriDetailService.loadUserByUsername("test@example.com");

        // Kontroller
        assertNotNull(userDetails, "UserDetails null dönmemeli.");
        assertEquals("test@example.com", userDetails.getUsername(), "Email yanlış dönüyor.");
        assertEquals("hashedpassword", userDetails.getPassword(), "Şifre yanlış dönüyor.");
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_MUSTERI")));
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        // Kullanıcı bulunamadığında boş bir sonuç döndürülüyor
        when(kullaniciRepository.findByEmail("notfound@example.com"))
                .thenReturn(Optional.empty());

        // Kullanıcı bulunamadığında UsernameNotFoundException fırlatılmalı
        Exception exception = assertThrows(UsernameNotFoundException.class, () ->
                musteriDetailService.loadUserByUsername("notfound@example.com"));

        assertEquals("Kullanıcı bulunamadı", exception.getMessage());
    }
}
