package org.appointment.backend.controller;

import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.service.KullaniciService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kullanici")
public class KullaniciController {
    private final KullaniciService kullaniciService;

    public KullaniciController(KullaniciService kullaniciService) {
        this.kullaniciService = kullaniciService;
    }

    @PostMapping
    public ResponseEntity<KullaniciDto> kaydet(@RequestBody KullaniciDto kullaniciDto) {
        return ResponseEntity.ok(kullaniciService.save(kullaniciDto));
    }

    @GetMapping
    public ResponseEntity<List<KullaniciDto>> tumunuListele() {
        return ResponseEntity.ok(kullaniciService.getAll());
    }
}