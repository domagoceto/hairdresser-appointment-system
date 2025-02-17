package org.appointment.backend.controller;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.service.ContactInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactInfoService contactInfoService;

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getContactInfo() {
        return ResponseEntity.ok(contactInfoService.getContactInfo());
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateContactInfo(@RequestBody Map<String, String> newContactInfo) {
        contactInfoService.updateContactInfo(newContactInfo);
        return ResponseEntity.ok("İletişim bilgileri başarıyla güncellendi.");
    }
}
