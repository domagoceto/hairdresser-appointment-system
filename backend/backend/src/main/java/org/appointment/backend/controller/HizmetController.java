package org.appointment.backend.controller;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.HizmetDto;
import org.appointment.backend.service.HizmetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hizmet")
@RequiredArgsConstructor
public class HizmetController {

    private final HizmetService hizmetService;

    @GetMapping({"/listeleHizmet"})
    public ResponseEntity<List<HizmetDto>> tumunuListele() {
        return ResponseEntity.ok(hizmetService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HizmetDto> getHizmetById(@PathVariable Long id) {
        return ResponseEntity.ok(hizmetService.getHizmetById(id));
    }

    @PostMapping({"/ekleHizmet"})
    public ResponseEntity<HizmetDto> kaydet(@RequestBody HizmetDto hizmetDto) {
        return ResponseEntity.ok(hizmetService.save(hizmetDto));
    }

    @PutMapping("/guncelle/{hizmetId}")
    public ResponseEntity<HizmetDto> guncelle(@PathVariable Long hizmetId, @RequestBody HizmetDto hizmetDto) {
        return ResponseEntity.ok(hizmetService.update(hizmetId, hizmetDto));
    }

    @DeleteMapping("/sil/{hizmetId}")
    public ResponseEntity<Void> sil(@PathVariable Long hizmetId) {
        hizmetService.delete(hizmetId);
        return ResponseEntity.noContent().build();
    }
}

