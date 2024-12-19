package org.appointment.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.entity.Kuafor;
import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.entity.Rol;
import org.appointment.backend.repo.KuaforRepository;
import org.appointment.backend.repo.KullaniciRepository;
import org.appointment.backend.service.KullaniciService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class KullaniciServiceImpl implements KullaniciService {

    private final KullaniciRepository kullaniciRepository;
    private final PasswordEncoder passwordEncoder;
    private final KuaforRepository kuaforRepository;

    @Override
    public KullaniciDto save(KullaniciDto kullaniciDto) {
        Kullanici kullanici = new Kullanici();
        kullanici.setAd(kullaniciDto.getAd());
        kullanici.setSoyad(kullaniciDto.getSoyad());
        kullanici.setEmail(kullaniciDto.getEmail());
        kullanici.setTelefon(kullaniciDto.getTelefon());
        kullanici.setCinsiyet(kullaniciDto.getCinsiyet());
        kullanici.setSifre(passwordEncoder.encode(kullaniciDto.getSifre()));

        // Rol belirleme
        if (kullaniciDto.getRol() != null) {
            kullanici.setRol(kullaniciDto.getRol());
        } else {
            kullanici.setRol(Rol.MUSTERI); // Varsayılan rol
        }

        // Kullanıcıyı kaydet
        Kullanici savedKullanici = kullaniciRepository.save(kullanici);

        log.info("Kullanıcı kaydedildi: {} - Rol: {}", savedKullanici.getEmail(), savedKullanici.getRol());
        return toDto(savedKullanici);
    }



    public KullaniciDto update(Long id, KullaniciDto kullaniciDto) {
        Kullanici mevcutKullanici = kullaniciRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        if (kullaniciDto.getAd() != null) {
            mevcutKullanici.setAd(kullaniciDto.getAd());
        }
        if (kullaniciDto.getSoyad() != null) {
            mevcutKullanici.setSoyad(kullaniciDto.getSoyad());
        }
        if (kullaniciDto.getTelefon() != null) {
            mevcutKullanici.setTelefon(kullaniciDto.getTelefon());
        }
        if (kullaniciDto.getSifre() != null && !kullaniciDto.getSifre().trim().isEmpty()) {
            mevcutKullanici.setSifre(kullaniciDto.getSifre());
        }

        kullaniciRepository.save(mevcutKullanici);
        return KullaniciDto.fromEntity(mevcutKullanici); // DTO'ya dönüştürüp döner.
    }


    @Override
    public void delete(Long kullaniciId) {
        Kullanici kullanici = kullaniciRepository.findById(kullaniciId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // Kuaför ilişkilerini bul ve sil
        List<Kuafor> kuaforler = kuaforRepository.findByKullanici_KullaniciId(kullaniciId);
        for (Kuafor kuafor : kuaforler) {
            kuaforRepository.delete(kuafor); // Kuaför kaydını sil
        }

        // Kullanıcıyı sil
        kullaniciRepository.deleteById(kullaniciId);
    }




    @Override
    public void deleteByEmail(String email) {
        Kullanici kullanici = kullaniciRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + email));
        kullaniciRepository.delete(kullanici);
    }


    @Override
    public Kullanici findEntityByEmail(String email) {
        return kullaniciRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + email));
    }


    @Override
    public KullaniciDto findByEmail(String email) {
        Kullanici kullanici = kullaniciRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));
        return toDto(kullanici);
    }

    @Override
    public List<KullaniciDto> getAll() {
        return kullaniciRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private KullaniciDto toDto(Kullanici kullanici) {
        return KullaniciDto.builder()
                .kullaniciId(kullanici.getKullaniciId())
                .ad(kullanici.getAd())
                .soyad(kullanici.getSoyad())
                .email(kullanici.getEmail())
                .telefon(kullanici.getTelefon())
                .cinsiyet(kullanici.getCinsiyet())
                .rol(kullanici.getRol())
                .build();
    }
}
