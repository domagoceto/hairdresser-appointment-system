package org.appointment.backend.controller;

import org.appointment.backend.dto.LoginRequest;
import org.appointment.backend.dto.RegisterRequest;
import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.dto.KullaniciUpdateRequest;
import org.appointment.backend.entity.Cinsiyet;
import org.appointment.backend.entity.Kuafor;
import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.entity.Rol;
import org.appointment.backend.security.JwtUtil;
import org.appointment.backend.service.KuaforService;
import org.appointment.backend.service.KullaniciService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final KullaniciService kullaniciService;
    private final PasswordEncoder passwordEncoder;
    private final KuaforService kuaforService;

    @Value("${admin.key}")
    private String VALID_ADMIN_KEY;

    @Value("${kuafor.key}")
    private String VALID_KUAFOR_KEY;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, KullaniciService kullaniciService, PasswordEncoder passwordEncoder, KuaforService kuaforService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.kullaniciService = kullaniciService;
        this.passwordEncoder = passwordEncoder;
        this.kuaforService = kuaforService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            log.info("Kayıt işlemi başlatıldı: {}", request);

            // E-posta kontrolü
            if (kullaniciService.existsByEmail(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Bu e-posta adresi ile zaten kayıt yapılmış.");
            }

            // Rolü belirle
            Rol rol = determineRole(request.getAdminKey(), request.getKuaforKey());
            log.info("Rol belirlendi: {}", rol);

            // Kullanıcıyı DTO'ya dönüştür
            KullaniciDto kullaniciDto = new KullaniciDto();
            kullaniciDto.setAd(request.getAd());
            kullaniciDto.setSoyad(request.getSoyad());
            kullaniciDto.setEmail(request.getEmail());
            kullaniciDto.setTelefon(request.getTelefon());
            kullaniciDto.setSifre(request.getSifre());
            kullaniciDto.setCinsiyet(Cinsiyet.valueOf(request.getCinsiyet().toUpperCase()));
            kullaniciDto.setRol(rol);

            log.info("Kullanıcı DTO hazırlandı: {}", kullaniciDto);

            // Kullanıcıyı kaydet
            KullaniciDto savedKullaniciDto = kullaniciService.save(kullaniciDto);
            log.info("Kullanıcı kaydedildi: {}", savedKullaniciDto);

            // Eğer rol KUAFOR ise Kuaför detaylarını ekle
            if (rol == Rol.KUAFOR) {
                log.info("Rol KUAFOR, Kuaför detayları ekleniyor...");
                String hashedPassword = passwordEncoder.encode(request.getSifre()); // Şifreyi hashle
                Kuafor kuafor = Kuafor.builder()
                        .ad(request.getAd())
                        .soyad(request.getSoyad())
                        .sifre(hashedPassword)
                        .telefon(request.getTelefon())
                        .email(request.getEmail())
                        .cinsiyet(Cinsiyet.valueOf(request.getCinsiyet().toUpperCase()))
                        .kullanici(new Kullanici(savedKullaniciDto.getKullaniciId())) // Kullanıcı ID ile ilişkilendir
                        .kuaforKey(request.getKuaforKey()) // Kuafor key'i ayarla
                        .build();

                kuaforService.save(kuafor);
                log.info("Kuaför başarıyla kaydedildi: {}", kuafor);
            }

            log.info("Kayıt başarılı: {}", savedKullaniciDto.getEmail());
            return ResponseEntity.ok("Kayıt başarılı: " + savedKullaniciDto.getEmail());
        } catch (Exception e) {
            log.error("Kayıt sırasında hata oluştu: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Kayıt işlemi başarısız: " + e.getMessage());
        }
    }

    // Rol belirleme metodu
    private Rol determineRole(String adminKey, String kuaforKey) {
        log.info("Admin key: {}, Kuaför key: {}", adminKey, kuaforKey);
        if (VALID_ADMIN_KEY.equals(adminKey)) {
            log.info("Admin rolü atanıyor.");
            return Rol.ADMIN;
        } else if (VALID_KUAFOR_KEY.equals(kuaforKey)) {
            log.info("Kuaför rolü atanıyor.");
            return Rol.KUAFOR;
        }
        log.info("Varsayılan olarak Müşteri rolü atanıyor.");
        return Rol.MUSTERI;
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // AuthenticationManager kullanılarak kimlik doğrulama
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // JWT Token oluşturma
            String token = jwtUtil.generateToken(authentication);
            log.info("Oluşturulan token: {}", token);

            // JSON formatında token döndürme
            return ResponseEntity.ok(Map.of("token", token));
        } catch (BadCredentialsException e) {
            log.error("Hatalı giriş: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Giriş başarısız: " + e.getMessage());
        } catch (Exception e) {
            log.error("Beklenmeyen bir hata oluştu: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Beklenmeyen bir hata oluştu.");
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
            if (updateRequest.getSifre() != null && !updateRequest.getSifre().trim().isEmpty()) {
                String hashedPassword = passwordEncoder.encode(updateRequest.getSifre());
                kullanici.setSifre(hashedPassword);
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
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteOwnAccount() {
        try {
            // JWT'den mevcut kullanıcının email bilgisini al
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

            // Kullanıcıyı sil
            kullaniciService.deleteByEmail(currentUserEmail);

            return ResponseEntity.ok("Hesap başarıyla silindi.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hesap silinirken hata oluştu: " + e.getMessage());
        }
    }
}
