package org.appointment.backend.controller;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.KuaforDto;
import org.appointment.backend.service.KuaforService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kuafor")
@RequiredArgsConstructor
public class KuaforController {

    private final KuaforService kuaforService;

    @GetMapping("/listeleKuafor")
    public ResponseEntity<List<KuaforDto>> tumunuListele() {

        return ResponseEntity.ok(kuaforService.getAll());
    }

    @PostMapping("/ekleKuafor")
    public ResponseEntity<KuaforDto> kaydet(@RequestBody KuaforDto kuaforDto) {
        return ResponseEntity.ok(kuaforService.save(kuaforDto));
    }

    @PutMapping("/update/{kuaforId}")
    public ResponseEntity<KuaforDto> guncelle(@PathVariable Long kuaforId, @RequestBody KuaforDto kuaforDto) {
        return ResponseEntity.ok(kuaforService.update(kuaforId, kuaforDto));
    }

    @DeleteMapping("/delete/{kuaforId}")
    public ResponseEntity<Void> sil(@PathVariable Long kuaforId) {
        kuaforService.delete(kuaforId);
        return ResponseEntity.noContent().build();
    }
}
