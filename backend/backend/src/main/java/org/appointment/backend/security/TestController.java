package org.appointment.backend.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/any-endpoint")
    public ResponseEntity<String> anyEndpoint() {
        return ResponseEntity.ok("Success");
    }
}

