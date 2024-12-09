package org.appointment.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/profile")
    @PreAuthorize("hasRole('ROLE_MUSTERI')")
    public String userProfile() {
        return "Kullanıcı profiline hoş geldiniz!";
    }
}
