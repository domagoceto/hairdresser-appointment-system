package org.appointment.backend.controller;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.KuaforDto;
import org.appointment.backend.entity.Kuafor;
import org.appointment.backend.service.KuaforService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kuafor")
@RequiredArgsConstructor
public class KuaforController {

    private final KuaforService kuaforService;

    @GetMapping({"/listeleKuafor"})
    public ResponseEntity<List<KuaforDto>> tumunuListele() {
        return ResponseEntity.ok(kuaforService.tumunuListele());
    }

    @GetMapping("/{kuaforId}")
    public ResponseEntity<KuaforDto> getKuaforById(@PathVariable Long id) {
        return ResponseEntity.ok(kuaforService.getKuaforById(id));
    }

    @PostMapping({"/ekleKuafor"})
    public ResponseEntity<KuaforDto> kaydet(@RequestBody KuaforDto kuaforDto) {
        return ResponseEntity.ok(kuaforService.kaydet(kuaforDto));
    }

    @PutMapping("/guncelle/{kuaforId}")
    public ResponseEntity<KuaforDto> guncelle(@PathVariable Long id, @RequestBody KuaforDto kuaforDto) {
        return ResponseEntity.ok(kuaforService.guncelle(id, kuaforDto));
    }

    @DeleteMapping("/sil/{kuaforId}")
    public ResponseEntity<Void> sil(@PathVariable Long id) {
        kuaforService.sil(id);
        return ResponseEntity.noContent().build();
    }
}