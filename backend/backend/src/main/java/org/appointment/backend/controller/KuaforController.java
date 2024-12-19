package org.appointment.backend.controller;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.KuaforDetailsResponse;
import org.appointment.backend.dto.KuaforRegisterRequest;
import org.appointment.backend.dto.KuaforUpdateRequest;
import org.appointment.backend.service.KuaforService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kuafor")
@RequiredArgsConstructor
public class KuaforController {

    private final KuaforService kuaforService;

    // Kuaför kaydını sadece ADMIN rolü yapabilir
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registerKuafor(@RequestBody KuaforRegisterRequest request) {
        try {
            kuaforService.registerKuafor(request);
            return ResponseEntity.ok("Kuaför başarıyla kayıt oldu.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Kuaför kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    // Kuaför, sadece kendi detaylarını görüntüleyebilir
    @GetMapping("/{kuaforId}")
    @PreAuthorize("hasRole('KUAFOR')")
    public ResponseEntity<KuaforDetailsResponse> getKuaforDetails(@PathVariable Long kuaforId, Authentication authentication) {
        String currentUserEmail = authentication.getName();

        // Kullanıcının kimliğini doğrulamak için email'i kontrol et
        KuaforDetailsResponse kuaforDetails = kuaforService.getKuaforDetails(kuaforId, currentUserEmail);

        if (kuaforDetails == null) {
            return ResponseEntity.status(404).body(null); // Kuaför bulunamazsa 404 döndürüyoruz.
        }

        return ResponseEntity.ok(kuaforDetails);
    }

    // Kuaför bilgilerini güncelleyebilir
    @PutMapping("/update/{kuaforId}")
    @PreAuthorize("hasRole('KUAFOR')")
    public ResponseEntity<String> updateKuaforInfo(@PathVariable Long kuaforId, @RequestBody KuaforUpdateRequest updateRequest, Authentication authentication) {
        String currentUserEmail = authentication.getName();

        // Güncelleme işlemi
        try {
            kuaforService.updateKuaforInfo(kuaforId, updateRequest, currentUserEmail);
            return ResponseEntity.ok("Kuaför bilgileri başarıyla güncellendi.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Kuaför bilgileri güncellenirken hata oluştu: " + e.getMessage());
        }
    }

    // Kuaför kendi randevularını görüntüleyebilir
    @GetMapping("/{kuaforId}/randevular")
    @PreAuthorize("hasRole('KUAFOR')")
    public ResponseEntity<?> getKuaforRandevular(@PathVariable Long kuaforId, Authentication authentication) {
        String currentUserEmail = authentication.getName();

        // Randevularını görüntüleme
        try {
            return ResponseEntity.ok(kuaforService.getKuaforRandevular(currentUserEmail));
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Randevular alınırken hata oluştu: " + e.getMessage());
        }
    }

    // Kuaför hizmet ekleyebilir
    @PostMapping("/{kuaforId}/hizmetler")
    @PreAuthorize("hasRole('KUAFOR')")
    public ResponseEntity<?> addServiceToKuafor(@PathVariable Long kuaforId, @RequestBody Long hizmetId) {
        try {
            kuaforService.addServiceToKuafor(kuaforId, hizmetId);
            return ResponseEntity.ok("Hizmet başarıyla eklendi.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Hizmet eklenirken hata oluştu: " + e.getMessage());
        }
    }
}
