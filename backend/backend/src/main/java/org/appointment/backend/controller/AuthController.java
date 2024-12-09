package org.appointment.backend.controller;

import org.appointment.backend.dto.LoginRequest;
import org.appointment.backend.dto.RegisterRequest;
import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.dto.KullaniciUpdateRequest;
import org.appointment.backend.entity.Cinsiyet;
import org.appointment.backend.entity.Rol;
import org.appointment.backend.security.JwtUtil;
import org.appointment.backend.service.KullaniciService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final KullaniciService kullaniciService;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.key}")
    private String adminKey;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, KullaniciService kullaniciService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.kullaniciService = kullaniciService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            KullaniciDto kullaniciDto = new KullaniciDto();
            kullaniciDto.setAd(registerRequest.getAd());
            kullaniciDto.setSoyad(registerRequest.getSoyad());
            kullaniciDto.setEmail(registerRequest.getEmail());
            kullaniciDto.setTelefon(registerRequest.getTelefon());
            kullaniciDto.setSifre(passwordEncoder.encode(registerRequest.getSifre()));
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

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody KullaniciUpdateRequest updateRequest, Authentication authentication) {
        try {
            // Token'dan mevcut kullanıcıyı al
            String email = authentication.getName();
            KullaniciDto kullanici = kullaniciService.findByEmail(email);

            if (kullanici == null) {
                return ResponseEntity.status(404).body("Kullanıcı bulunamadı!");
            }

            // İstekten gelen bilgilerle güncelleme
            if (updateRequest.getAd() != null) {
                kullanici.setAd(updateRequest.getAd());
            }
            if (updateRequest.getSoyad() != null) {
                kullanici.setSoyad(updateRequest.getSoyad());
            }
            if (updateRequest.getTelefon() != null) {
                kullanici.setTelefon(updateRequest.getTelefon());
            }
            if (updateRequest.getSifre() != null) {
                // Şifreyi hash'le
                kullanici.setSifre(passwordEncoder.encode(updateRequest.getSifre()));
            }
            if (updateRequest.getEmail() != null) {
                kullanici.setEmail(updateRequest.getEmail());
            }

            // Güncellemeyi kaydet
            kullaniciService.update(kullanici.getKullaniciId(), kullanici);

            return ResponseEntity.ok("Kullanıcı bilgileri başarıyla güncellendi!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Kullanıcı bilgileri güncellenirken hata oluştu: " + e.getMessage());
        }
    }

    // Kullanıcı kendi hesabını silebilir
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteOwnAccount() {
        try {
            // JWT'den kullanıcı adını (email) al
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            kullaniciService.deleteByEmail(currentUserEmail);
            return ResponseEntity.ok("Hesap başarıyla silindi.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Hesap silinirken bir hata oluştu: " + e.getMessage());
        }
    }
}
