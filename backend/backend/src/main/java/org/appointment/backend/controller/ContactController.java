package org.appointment.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {

    private static final Map<String, String> contactInfo = new HashMap<>();

    static {
        contactInfo.put("adres", "");
        contactInfo.put("email", "");
        contactInfo.put("telefon", "");
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getContactInfo() {
        return ResponseEntity.ok(contactInfo);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateContactInfo(@RequestBody Map<String, String> newContactInfo) {
        contactInfo.putAll(newContactInfo);
        return ResponseEntity.ok("İletişim bilgileri güncellendi.");
    }
}
