package org.appointment.backend.controller;

import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.service.KullaniciService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class KullaniciController {

    private final KullaniciService kullaniciService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Sadece ADMIN rolü kullanıcı ekleyebilir
    public ResponseEntity<KullaniciDto> kaydet(@RequestBody KullaniciDto kullaniciDto) {
        return ResponseEntity.ok(kullaniciService.save(kullaniciDto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')") // ADMIN ve MUSTERI kullanıcıları listeleyebilir
    public ResponseEntity<List<KullaniciDto>> tumunuListele() {
        return ResponseEntity.ok(kullaniciService.getAll());
    }

    @PutMapping("guncelle/{kullaniciId}")
    @PreAuthorize("hasRole('ADMIN')") // Sadece ADMIN güncelleyebilir
    public ResponseEntity<KullaniciDto> guncelle(@PathVariable Long kullaniciId, @RequestBody KullaniciDto kullaniciDto) {
        return ResponseEntity.ok(kullaniciService.update(kullaniciId, kullaniciDto));
    }

    @DeleteMapping("sil/{kullaniciId}")
    @PreAuthorize("hasRole('ADMIN')") // Sadece ADMIN silebilir
    public ResponseEntity<Void> sil(@PathVariable Long kullaniciId) {
        kullaniciService.delete(kullaniciId);
        return ResponseEntity.noContent().build();
    }
}
