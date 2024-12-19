package org.appointment.backend.controller;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.dto.RandevuDto;
import org.appointment.backend.service.KullaniciService;
import org.appointment.backend.service.RandevuService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<KullaniciDto>> getAllUsers() {
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
    public ResponseEntity<List<RandevuDto>> getAllAppointments() {
        return ResponseEntity.ok(randevuService.getAllRandevular()); // randevuService'i kullandık
    }
}
