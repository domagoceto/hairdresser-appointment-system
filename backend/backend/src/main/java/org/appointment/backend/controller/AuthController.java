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
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
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
            log.debug("Kayıt işlemi başladı. Gelen bilgiler: {}", registerRequest);

            KullaniciDto kullaniciDto = new KullaniciDto();
            kullaniciDto.setAd(registerRequest.getAd());
            kullaniciDto.setSoyad(registerRequest.getSoyad());
            kullaniciDto.setEmail(registerRequest.getEmail());
            kullaniciDto.setTelefon(registerRequest.getTelefon());
            kullaniciDto.setSifre(registerRequest.getSifre());

            if (registerRequest.getCinsiyet() != null) {
                kullaniciDto.setCinsiyet(Cinsiyet.valueOf(registerRequest.getCinsiyet().toUpperCase()));
            } else {
                log.warn("Cinsiyet null, varsayılan olarak ERKEK atanıyor.");
                kullaniciDto.setCinsiyet(Cinsiyet.ERKEK); // Varsayılan değer
            }

            // Rol ataması
            if (adminKey.equals(registerRequest.getAdminKey())) {
                kullaniciDto.setRol(Rol.ADMIN);
            } else {
                kullaniciDto.setRol(Rol.MUSTERI);
            }

            KullaniciDto savedUser = kullaniciService.save(kullaniciDto);
            log.debug("Kayıt işlemi başarılı. Kaydedilen kullanıcı: {}", savedUser);

            return ResponseEntity.ok("Kayıt başarılı: " + savedUser.getEmail());
        } catch (Exception e) {
            log.error("Kayıt sırasında hata oluştu: ", e);
            return ResponseEntity.status(400).body("Kayıt sırasında hata oluştu: " + e.getMessage());
        }
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            log.info("Giriş yapılan kullanıcı adı: {}", loginRequest.getUsername());
            log.info("Giriş yapılan şifre: {}", loginRequest.getPassword());

            KullaniciDto kullanici = kullaniciService.findByEmail(loginRequest.getUsername());
            if (kullanici == null) {
                log.warn("Kullanıcı bulunamadı: {}", loginRequest.getUsername());
                return ResponseEntity.status(404).body("Kullanıcı bulunamadı.");
            }

            log.info("Veritabanındaki hashlenmiş şifre: {}", kullanici.getSifre());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            String token = jwtUtil.generateToken(authentication);
            return ResponseEntity.ok("Bearer " + token);
        } catch (Exception e) {
            log.error("Login sırasında hata oluştu: ", e);
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
                System.out.println("Güncelleme sırasında kullanıcı bulunamadı: " + email);
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
                String hashedPassword = passwordEncoder.encode(updateRequest.getSifre());
                System.out.println("Güncellenen hashlenmiş şifre: " + hashedPassword);
                kullanici.setSifre(hashedPassword);
            }
            if (updateRequest.getEmail() != null) {
                System.out.println("Güncellenen e-mail: " + updateRequest.getEmail());
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
