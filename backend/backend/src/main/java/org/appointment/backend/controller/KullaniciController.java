package org.appointment.backend.controller;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.service.KullaniciService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class KullaniciController {

    private final KullaniciService kullaniciService;

    // Müşteri kendi bilgilerini güncelleyebilir
    @PutMapping("/update")
    public ResponseEntity<KullaniciDto> updateUser(@RequestBody KullaniciDto kullaniciDto) {
        // Oturum açan kullanıcının email bilgisi alınır
        String email = getAuthenticatedUserEmail();

        // Oturum açan kullanıcı bilgisi alınır
        KullaniciDto existingUser = kullaniciService.findByEmail(email);

        // Güncellenmesi gereken alanlar belirlenir
        if (kullaniciDto.getTelefon() != null) {
            existingUser.setTelefon(kullaniciDto.getTelefon());
        }

        // Güncelleme işlemi yapılır
        KullaniciDto updatedUser = kullaniciService.update(existingUser.getKullaniciId(), existingUser);

        return ResponseEntity.ok(updatedUser);
    }


    // Müşteri kendi hesabını silebilir
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser() {
        // Kimlik doğrulanan kullanıcının email adresini al
        String email = getAuthenticatedUserEmail();

        // Kullanıcı bilgilerini bul
        KullaniciDto existingUser = kullaniciService.findByEmail(email);

        // Kullanıcıyı sil
        kullaniciService.delete(existingUser.getKullaniciId());

        return ResponseEntity.noContent().build();
    }

    // Kimlik doğrulanan kullanıcının email adresini al
    private String getAuthenticatedUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

}
