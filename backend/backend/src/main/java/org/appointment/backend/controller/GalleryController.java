package org.appointment.backend.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/gallery")
@RequiredArgsConstructor
public class GalleryController {

    private static final String UPLOAD_DIR = "uploads/gallery/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dosya yüklenemedi.");
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            String imageUrl = "/uploads/gallery/" + fileName;
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dosya yüklenirken hata oluştu.");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listGalleryImages() {
        try (Stream<Path> paths = Files.walk(Paths.get(UPLOAD_DIR))) {
            List<String> imageUrls = paths
                    .filter(Files::isRegularFile)
                    .map(path -> "/uploads/gallery/" + path.getFileName().toString())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(imageUrls);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete/{fileName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteImage(@PathVariable String fileName) {
        try {
            Path path = Paths.get(UPLOAD_DIR + fileName);
            if (Files.exists(path)) {
                Files.delete(path);
                return ResponseEntity.ok("Resim başarıyla silindi.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resim bulunamadı.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Resim silinirken hata oluştu.");
        }
    }
}
