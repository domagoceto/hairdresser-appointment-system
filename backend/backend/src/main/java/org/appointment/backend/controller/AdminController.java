package org.appointment.backend.controller;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.AdminKullaniciDto;
import org.appointment.backend.dto.KuaforRandevuDto;
import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.dto.RandevuDto;
import org.appointment.backend.service.KullaniciService;
import org.appointment.backend.service.RandevuService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final KullaniciService kullaniciService;
    private final RandevuService randevuService; // RandevuService'i ekledik

    @PostMapping("/kullanici")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<KullaniciDto> addUser(@RequestBody KullaniciDto dto) {
        return ResponseEntity.ok(kullaniciService.save(dto));
    }

    @GetMapping("/kullanicilar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminKullaniciDto>> getAllUsers() {
        return ResponseEntity.ok(kullaniciService.getAll());
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
