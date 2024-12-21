package org.appointment.backend.controller;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.*;
import org.appointment.backend.repo.KullaniciRepository;
import org.appointment.backend.service.KuaforService;
import org.appointment.backend.service.KullaniciService;
import org.appointment.backend.service.RandevuService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final KullaniciService kullaniciService;
    private final KuaforService kuaforService;
    private final RandevuService randevuService;
    private final KullaniciRepository kullaniciRepository;// RandevuService'i ekledik

    @PostMapping("/kullanici")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<KullaniciDto> addUser(@RequestBody KullaniciDto dto) {
        return ResponseEntity.ok(kullaniciService.save(dto));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<KullaniciDto> getCurrentAdmin(Authentication authentication) {
        // Authentication'dan email alınır
        String email = authentication.getName();
        // Email'e göre kullanıcı bilgisi bulunur
        KullaniciDto adminDto = kullaniciService.findByEmail(email);
        return ResponseEntity.ok(adminDto);
    }

    @GetMapping("/kuaforler") // Yeni endpoint
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<KuaforDto>> getAllKuaforler() {
        try {
            List<KuaforDto> kuaforler = kuaforService.getAllKuaforler();
            return ResponseEntity.ok(kuaforler);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    @GetMapping("/kullanicilar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminKullaniciDto>> getAllUsers() {
        List<AdminKullaniciDto> users = kullaniciRepository.findAll().stream()
                .map(user -> AdminKullaniciDto.builder()
                        .ad(user.getAd())
                        .soyad(user.getSoyad())
                        .email(user.getEmail())
                        .telefon(user.getTelefon())
                        .cinsiyet(user.getCinsiyet().name())
                        .rol(user.getRol().name()) // Rol bilgisini ekliyoruz
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }


    @PutMapping("/kullanici/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<KullaniciDto> updateUser(@PathVariable Long id, @RequestBody KullaniciDto dto) {
        return ResponseEntity.ok(kullaniciService.update(id, dto));
    }

    @DeleteMapping("/kullanici/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        kullaniciService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/randevular")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<KuaforRandevuDto>> getFilteredAppointments(
            @RequestParam Long kuaforId,
            @RequestParam String tarih // Tarihi String olarak alıp LocalDate'e çevireceğiz
    ) {
        try {
            LocalDate localDate = LocalDate.parse(tarih); // String tarihi LocalDate'e çeviriyoruz
            List<KuaforRandevuDto> randevular = randevuService.getRandevularByKuaforAndTarih(kuaforId, localDate);
            return ResponseEntity.ok(randevular);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

}
