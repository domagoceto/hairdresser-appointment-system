package org.appointment.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.appointment.backend.dto.OdemeDto;
import org.appointment.backend.entity.OdemeDurum;
import org.appointment.backend.entity.OdemeYontemi;
import org.appointment.backend.service.OdemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/odeme")
@RequiredArgsConstructor
public class OdemeController {

    private final OdemeService odemeService;

    @GetMapping("/admin/listele")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OdemeDto>> tumOdemeleriListele() {
        List<OdemeDto> odemeler = odemeService.getAll();
        return ResponseEntity.ok(odemeler);
    }


    @PutMapping("/admin/guncelle/{odemeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OdemeDto> odemeGuncelle(@PathVariable Long odemeId, @RequestBody OdemeDto odemeDto) {
        if (odemeId == null) {
            throw new IllegalArgumentException("Ödeme ID'si eksik!");
        }
        OdemeDto updatedOdeme = odemeService.update(odemeId, odemeDto);
        return ResponseEntity.ok(updatedOdeme);
    }


    @PostMapping("/admin/ekle")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OdemeDto> ekleOdeme(@RequestBody OdemeDto odemeDto) {
        if (odemeDto.getKullaniciId() == null || odemeDto.getRandevuId() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            log.info("Ekleme işlemi başlatıldı. Gelen DTO: {}", odemeDto);
            OdemeDto savedOdeme = odemeService.save(odemeDto);
            return ResponseEntity.ok(savedOdeme);
        } catch (Exception e) {
            log.error("Ödeme ekleme hatası: {}", e.getMessage());
            return ResponseEntity.status(400).body(null);
        }
    }


    @GetMapping("/yontemler")
    public ResponseEntity<List<OdemeYontemi>> getOdemeYontemleri() {
        return ResponseEntity.ok(Arrays.asList(OdemeYontemi.values()));
    }

    @GetMapping("/durumlar")
    public ResponseEntity<List<OdemeDurum>> getOdemeDurumlari() {
        return ResponseEntity.ok(Arrays.asList(OdemeDurum.values()));
    }

}
