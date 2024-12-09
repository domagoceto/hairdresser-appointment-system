package org.appointment.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/protected-endpoint")
public class ProtectedController {

    @GetMapping
    public ResponseEntity<String> getProtectedData() {
        return ResponseEntity.ok("Bu korumalı bir endpointtir. Erişim başarılı!");
    }
}
