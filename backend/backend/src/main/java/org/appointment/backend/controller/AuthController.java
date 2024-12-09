package org.appointment.backend.controller;

import org.appointment.backend.dto.LoginRequest;
import org.appointment.backend.dto.RegisterRequest;
import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.entity.Cinsiyet;
import org.appointment.backend.entity.Rol;
import org.appointment.backend.security.JwtUtil;
import org.appointment.backend.service.KullaniciService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final KullaniciService kullaniciService;

    @Value("${admin.key}")
    private String adminKey;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, KullaniciService kullaniciService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.kullaniciService = kullaniciService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            KullaniciDto kullaniciDto = new KullaniciDto();
            kullaniciDto.setAd(registerRequest.getAd());
            kullaniciDto.setSoyad(registerRequest.getSoyad());
            kullaniciDto.setEmail(registerRequest.getEmail());
            kullaniciDto.setTelefon(registerRequest.getTelefon());
            kullaniciDto.setSifre(registerRequest.getSifre());
            kullaniciDto.setCinsiyet(Cinsiyet.valueOf(registerRequest.getCinsiyet()));

            // Admin key kontrolü
            if (adminKey.equals(registerRequest.getAdminKey())) {
                kullaniciDto.setRol(Rol.ADMIN);
            } else {
                kullaniciDto.setRol(Rol.MUSTERI);
            }

            // Kullanıcıyı kaydet
            KullaniciDto savedUser = kullaniciService.save(kullaniciDto);

            return ResponseEntity.ok("Kayıt başarılı: " + savedUser.getEmail());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Kayıt sırasında hata oluştu: " + e.getMessage());
        }
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            String token = jwtUtil.generateToken(authentication);
            return ResponseEntity.ok("Bearer " + token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Giriş başarısız: " + e.getMessage());
        }
    }
}

