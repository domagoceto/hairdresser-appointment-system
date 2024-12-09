package org.appointment.backend.service.impl;

import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.repo.KullaniciRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KullaniciServiceImplTest {

    private final KullaniciRepository kullaniciRepository = mock(KullaniciRepository.class); // Kullanıcı repository'sinin mock'u
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class); // PasswordEncoder'ın mock'u

    // Test edilen sınıf
    private final KullaniciServiceImpl kullaniciService =
            new KullaniciServiceImpl(kullaniciRepository, passwordEncoder);

    @Test
    void testSaveKullanici() {
        // Test verileri
        KullaniciDto kullaniciDto = new KullaniciDto();
        kullaniciDto.setAd("Test");
        kullaniciDto.setSoyad("User");
        kullaniciDto.setEmail("test@example.com");
        kullaniciDto.setSifre("password");

        // Şifre hashleme mock davranışı
        when(passwordEncoder.encode("password")).thenReturn("hashedpassword");

        // Kaydedilecek kullanıcı mock'u
        Kullanici savedKullanici = new Kullanici();
        savedKullanici.setAd("Test");
        savedKullanici.setSoyad("User");
        savedKullanici.setEmail("test@example.com");
        savedKullanici.setSifre("hashedpassword");

        // Kullanıcı kaydetme mock davranışı
        when(kullaniciRepository.save(Mockito.any(Kullanici.class))).thenReturn(savedKullanici);

        // Test edilen metodun çağrılması
        KullaniciDto result = kullaniciService.save(kullaniciDto);

        // Kontroller
        assertNotNull(result); // Sonuç boş olmamalı
        assertEquals("Test", result.getAd()); // Ad kontrolü
        assertEquals("User", result.getSoyad()); // Soyad kontrolü
        assertEquals("test@example.com", result.getEmail()); // Email kontrolü
    }
}
