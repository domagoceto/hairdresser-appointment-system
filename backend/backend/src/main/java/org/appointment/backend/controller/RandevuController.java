package org.appointment.backend.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.dto.RandevuDto;

import org.appointment.backend.dto.RandevuRequest;
import org.appointment.backend.dto.RandevuResponse;
import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.repo.KullaniciRepository;
import org.appointment.backend.repo.RandevuRepository;
import org.appointment.backend.service.RandevuService;
import org.appointment.backend.service.KuaforService;
import org.appointment.backend.service.KullaniciService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/randevu")
@RequiredArgsConstructor
public class RandevuController {

    private final RandevuService randevuService;
    private final KuaforService kuaforService;
    private final KullaniciService kullaniciService;
    private final KullaniciRepository kullaniciRepository;
    private final RandevuRepository randevuRepository;


    // Müşteri randevu alabilir
    @PostMapping("/al")
    @PreAuthorize("hasRole('MUSTERI')")
    public ResponseEntity<?> alRandevu(@RequestBody RandevuDto randevuDto) {
        try {
            // Oturum açmış kullanıcının email adresini alın
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            // Kullanıcı bilgilerini bulun
            KullaniciDto kullanici = kullaniciService.findByEmail(email);
            if (kullanici == null) {
                return ResponseEntity.status(404).body("Kullanıcı bulunamadı.");
            }

            // Kullanıcı ID'yi randevu DTO'ya ekleyin
            randevuDto.setKullaniciId(kullanici.getKullaniciId());

            // Randevuyu oluştur
            RandevuDto randevu = randevuService.alRandevu(randevuDto);
            return ResponseEntity.ok(randevu);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Randevu veya kullanıcı bulunamadı: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Randevu alınırken bir hata oluştu: " + e.getMessage());
        }
    }

    @PostMapping("/randevu")
    @PreAuthorize("hasRole('MUSTERI')") // Controller'da MUSTERI" rolü için randevu oluşturma kodlaması
    public ResponseEntity<RandevuResponse> createRandevu(@RequestBody RandevuRequest request) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Kullanici kullanici = kullaniciRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

            RandevuResponse response = randevuService.createRandevu(request, kullanici.getKullaniciId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }



    // Müşteri randevu bilgilerini görüntüleyebilir
    @GetMapping("/görüntüle/{randevuId}")
    @PreAuthorize("hasRole('MUSTERI')") // Müşteri sadece kendi randevusunu görebilir
    public ResponseEntity<RandevuDto> getRandevu(@PathVariable Long randevuId) {
        try {
            RandevuDto randevu = randevuService.getRandevu(randevuId);
            return ResponseEntity.ok(randevu);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/goruntule/kullanici")
    @PreAuthorize("hasRole('MUSTERI')")
    public ResponseEntity<List<RandevuDto>> getKullaniciRandevulari() {
        try {
            // Kullanıcının email'ini JWT'den al
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            // Kullanıcı bilgilerini bul
            Kullanici kullanici = kullaniciService.findEntityByEmail(email);

            // Kullanıcıya ait randevuları getir
            List<RandevuDto> randevular = randevuService.getRandevularByKullaniciId(kullanici.getKullaniciId());
            return ResponseEntity.ok(randevular);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PutMapping("/guncelle/{randevuId}")
    @PreAuthorize("hasRole('MUSTERI')")
    public ResponseEntity<?> guncelleRandevu(
            @PathVariable Long randevuId,
            @RequestBody RandevuDto randevuDto
    ) {
        try {
            // Kullanıcının kimliği alınıyor
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Kullanici kullanici = kullaniciService.findEntityByEmail(email);

            // Randevuyu güncelle
            RandevuDto updatedRandevu = randevuService.guncelleRandevu(randevuId, randevuDto, kullanici.getKullaniciId());
            return ResponseEntity.ok(updatedRandevu);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Randevu güncellenirken hata oluştu: " + e.getMessage());
        }
    }

    @PutMapping("/iptal/{randevuId}")
    @PreAuthorize("hasRole('MUSTERI')")
    public ResponseEntity<?> iptalRandevu(@PathVariable Long randevuId) {
        try {
            // Kullanıcının kimliği alınıyor
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Kullanici kullanici = kullaniciService.findEntityByEmail(email);

            // Randevuyu iptal et
            randevuService.iptalRandevu(randevuId, kullanici.getKullaniciId());
            return ResponseEntity.ok("Randevu başarıyla iptal edildi.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Randevu iptal edilirken hata oluştu: " + e.getMessage());
        }
    }

    @DeleteMapping("/randevu/{id}/sil")
    public ResponseEntity<?> deleteRandevu(@PathVariable Long id) {
        try {
            if (!randevuRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Silinmek istenen randevu bulunamadı.");
            }

            randevuRepository.deleteById(id);
            return ResponseEntity.ok("Randevu başarıyla silindi.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Randevu silinirken bir hata oluştu: " + e.getMessage());
        }
    }





    // Admin tüm randevuları görüntüleyebilir
    @GetMapping("/admin/görüntüle")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRandevular() {
        try {
            return ResponseEntity.ok(randevuService.getAllRandevular());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Randevular alınırken hata oluştu.");
        }
    }

}
