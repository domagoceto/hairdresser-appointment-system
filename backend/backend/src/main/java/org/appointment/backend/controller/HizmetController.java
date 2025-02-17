package org.appointment.backend.controller;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.HizmetDto;
import org.appointment.backend.dto.HizmetEkleRequest;
import org.appointment.backend.entity.Hizmet;
import org.appointment.backend.repo.HizmetRepository;
import org.appointment.backend.service.HizmetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hizmet")
@RequiredArgsConstructor
public class HizmetController {

    private final HizmetService hizmetService;
    private final HizmetRepository hizmetRepository;

    // Admin hizmet ekleyebilir
    @PostMapping(value = "/ekle", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> hizmetEkle(
            @RequestParam("ad") String ad,
            @RequestParam("aciklama") String aciklama,
            @RequestParam("fiyat") Double fiyat,
            @RequestParam("sure") Integer sure,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            String imageUrl = null;
            if (image != null && !image.isEmpty()) {
                imageUrl = uploadImage(image);
            }

            Hizmet hizmet = new Hizmet();
            hizmet.setAd(ad);
            hizmet.setAciklama(aciklama);
            hizmet.setFiyat(fiyat);
            hizmet.setSure(sure);
            hizmet.setImageUrl(imageUrl);

            Hizmet savedHizmet = hizmetRepository.save(hizmet);
            return ResponseEntity.ok(savedHizmet);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Hizmet eklenirken hata oluştu: " + e.getMessage());
        }
    }

    private String uploadImage(MultipartFile file) throws IOException {
        String folderPath = System.getProperty("user.dir") + "/uploads/"; // **Proje ana dizinine kaydet**
        Files.createDirectories(Paths.get(folderPath)); // **Eğer klasör yoksa oluştur**

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Paths.get(folderPath + fileName);
        Files.write(path, file.getBytes());

        return "/uploads/" + fileName; // **Frontend için sadece "/uploads/" döndür**
    }




    @DeleteMapping("/sil/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Sadece ADMIN rolüne izin
    public ResponseEntity<?> hizmetSil(@PathVariable Long id) {
        try {
            hizmetService.hizmetSil(id);
            return ResponseEntity.ok("Hizmet başarıyla silindi.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Hizmet bulunamadı: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Hizmet silinirken hata oluştu: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<HizmetDto>> getAllHizmetler() {
        List<Hizmet> hizmetler = hizmetService.getAllHizmetler();
        List<HizmetDto> hizmetDTOList = hizmetler.stream()
                .map(h -> new HizmetDto(h.getHizmetId(), h.getAd(), h.getAciklama(), h.getFiyat(), h.getSure(), h.getImageUrl()))
                .toList();
        return ResponseEntity.ok(hizmetDTOList);
    }



}




