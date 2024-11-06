package org.appointment.backend.controller;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.KuaforDto;
import org.appointment.backend.entity.Kuafor;
import org.appointment.backend.service.KuaforService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kuaforler")
@RequiredArgsConstructor
public class KuaforController {

    private final KuaforService kuaforService;

    @GetMapping
    public ResponseEntity<List<KuaforDto>> getAllKuaforler() {
        return ResponseEntity.ok(kuaforService.getAllKuaforler());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KuaforDto> getKuaforById(@PathVariable Long id) {
        return ResponseEntity.ok(kuaforService.getKuaforById(id));
    }

    @PostMapping
    public ResponseEntity<KuaforDto> createKuafor(@RequestBody KuaforDto kuaforDto) {
        return ResponseEntity.ok(kuaforService.createKuafor(kuaforDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KuaforDto> updateKuafor(@PathVariable Long id, @RequestBody KuaforDto kuaforDto) {
        return ResponseEntity.ok(kuaforService.updateKuafor(id, kuaforDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKuafor(@PathVariable Long id) {
        kuaforService.deleteKuafor(id);
        return ResponseEntity.noContent().build();
    }
}