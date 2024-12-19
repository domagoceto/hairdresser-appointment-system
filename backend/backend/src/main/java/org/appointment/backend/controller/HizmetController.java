package org.appointment.backend.controller;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.HizmetDto;
import org.appointment.backend.dto.HizmetEkleRequest;
import org.appointment.backend.entity.Hizmet;
import org.appointment.backend.service.HizmetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hizmet")
@RequiredArgsConstructor
public class HizmetController {

    private final HizmetService hizmetService;

    // Admin hizmet ekleyebilir
    @PostMapping("/ekle")
    @PreAuthorize("hasRole('ADMIN')") // Sadece ADMIN rolüne izin verilir
    public ResponseEntity<?> hizmetEkle(@RequestBody HizmetDto hizmetDto) {
        try {
            HizmetDto yeniHizmet = hizmetService.hizmetEkle(hizmetDto);
            return ResponseEntity.ok(yeniHizmet);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Hizmet eklenirken hata oluştu: " + e.getMessage());
        }
    }
}




