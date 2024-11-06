package org.appointment.backend.controller;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.HizmetDto;
import org.appointment.backend.service.HizmetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hizmetler")
@RequiredArgsConstructor
public class HizmetController {

    private final HizmetService hizmetService;

    @GetMapping
    public ResponseEntity<List<HizmetDto>> getAllHizmetler() {
        return ResponseEntity.ok(hizmetService.getAllHizmetler());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HizmetDto> getHizmetById(@PathVariable Long id) {
        return ResponseEntity.ok(hizmetService.getHizmetById(id));
    }

    @PostMapping
    public ResponseEntity<HizmetDto> createHizmet(@RequestBody HizmetDto hizmetDto) {
        return ResponseEntity.ok(hizmetService.createHizmet(hizmetDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HizmetDto> updateHizmet(@PathVariable Long id, @RequestBody HizmetDto hizmetDto) {
        return ResponseEntity.ok(hizmetService.updateHizmet(id, hizmetDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHizmet(@PathVariable Long id) {
        hizmetService.deleteHizmet(id);
        return ResponseEntity.noContent().build();
    }
}