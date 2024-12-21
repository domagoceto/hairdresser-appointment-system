package org.appointment.backend.controller;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.*;
import org.appointment.backend.entity.Hizmet;
import org.appointment.backend.entity.Kuafor;
import org.appointment.backend.repo.HizmetRepository;
import org.appointment.backend.service.KuaforService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/kuafor")
@RequiredArgsConstructor
public class KuaforController {

    private final KuaforService kuaforService;
    private final HizmetRepository hizmetRepository;

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

    // Kuaför kendi bilgilerini görüntüleyebilir
    @GetMapping("/me")
    @PreAuthorize("hasRole('KUAFOR')") // Sadece 'KUAFOR' rolüne sahip kullanıcılar erişebilir
    public ResponseEntity<KuaforDetailsResponse> getCurrentKuafor(Authentication authentication) {
        String email = authentication.getName(); // Giriş yapan kullanıcının email'ini al
        KuaforDetailsResponse kuaforDetails = kuaforService.getKuaforByEmailForDetails(email); // Servis çağrısı
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
    public ResponseEntity<List<KuaforRandevuResponseDto>> getKuaforRandevular(
            @PathVariable Long kuaforId,
            @RequestParam("tarih") LocalDate tarih,
            Authentication authentication) {

        String email = authentication.getName();

        // Servis çağrısı
        List<KuaforRandevuResponseDto> randevular = kuaforService.getKuaforRandevular(email, kuaforId, tarih);

        return ResponseEntity.ok(randevular);
    }



    // Kuaför hizmet ekleyebilir
    @PostMapping("/{kuaforId}/hizmetler")
    @PreAuthorize("hasRole('KUAFOR')")
    public ResponseEntity<?> addServiceToKuafor(@PathVariable Long kuaforId, @RequestBody Long hizmetId) {
        try {
            // Oturum açmış kuaförün email bilgisi alınır
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            // Kuaförün sahibi olduğu ID kontrol edilir
            Kuafor kuafor = kuaforService.findKuaforByEmail(email);

            if (!kuafor.getKuaforId().equals(kuaforId)) {
                return ResponseEntity.status(403).body("Bu kuaföre hizmet ekleme yetkiniz yok.");
            }

            // Hizmet ekleme işlemi yapılır
            kuaforService.addServiceToKuafor(kuaforId, hizmetId);
            return ResponseEntity.ok("Hizmet başarıyla eklendi.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Hizmet eklenirken hata oluştu: " + e.getMessage());
        }
    }

    @GetMapping("/hizmetler")
    @PreAuthorize("hasRole('KUAFOR')")
    public ResponseEntity<List<HizmetDto>> getAllHizmetler() {
        try {
            List<Hizmet> hizmetler = hizmetRepository.findAll();
            // Hizmetleri DTO'ya dönüştür
            List<HizmetDto> hizmetDtos = hizmetler.stream()
                    .map(hizmet -> {
                        HizmetDto dto = new HizmetDto();
                        dto.setHizmetId(hizmet.getHizmetId());
                        dto.setAd(hizmet.getAd());
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(hizmetDtos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }




    // Yeni endpoint: Kuaförün hizmetlerini getir
    @GetMapping("/{kuaforId}/hizmetler")
    @PreAuthorize("hasRole('KUAFOR')")
    public ResponseEntity<List<String>> getKuaforHizmetler(
            @PathVariable Long kuaforId,
            Authentication authentication) {

        String email = authentication.getName();
        List<String> hizmetler = kuaforService.getKuaforHizmetler(email, kuaforId);
        return ResponseEntity.ok(hizmetler);
    }



}
