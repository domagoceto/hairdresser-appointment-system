package org.appointment.backend.controller;

import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.service.KullaniciService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kullanici")
@RequiredArgsConstructor
public class KullaniciController {

    private final KullaniciService kullaniciService;

    @PostMapping
    public ResponseEntity<KullaniciDto> kaydet(@RequestBody KullaniciDto kullaniciDto) {
        return ResponseEntity.ok(kullaniciService.save(kullaniciDto));
    }
    @GetMapping
    public ResponseEntity<List<KullaniciDto>>tumunuListele() {
        return ResponseEntity.ok(kullaniciService.getAll());
    }
}