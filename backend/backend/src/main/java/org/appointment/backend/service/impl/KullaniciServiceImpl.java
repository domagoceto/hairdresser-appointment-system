package org.appointment.backend.service.impl;

import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.repo.KullaniciRepository;
import org.appointment.backend.service.KullaniciService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class KullaniciServiceImpl implements KullaniciService {

    private final KullaniciRepository kullaniciRepository;
    private final PasswordEncoder passwordEncoder;

    public KullaniciServiceImpl(KullaniciRepository kullaniciRepository, PasswordEncoder passwordEncoder) {
        this.kullaniciRepository = kullaniciRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public KullaniciDto save(KullaniciDto kullaniciDto) {
        Kullanici kullanici = new Kullanici();

        // Şifre alanı kontrolü
        if (kullaniciDto.getSifre() == null || kullaniciDto.getSifre().isBlank()) {
            throw new IllegalArgumentException("Şifre alanı boş olamaz.");
        }

        // Kullanıcı bilgilerini DTO'dan al
        kullanici.setAd(kullaniciDto.getAd());
        kullanici.setSoyad(kullaniciDto.getSoyad());
        kullanici.setEmail(kullaniciDto.getEmail());
        kullanici.setTelefon(kullaniciDto.getTelefon());
        kullanici.setRol(kullaniciDto.getRol());
        kullanici.setCinsiyet(kullaniciDto.getCinsiyet());

        // Şifreyi hash'le ve kaydet
        String hashedPassword = passwordEncoder.encode(kullaniciDto.getSifre());
        kullanici.setSifre(hashedPassword);

        // Kullanıcıyı kaydet ve geri döndür
        Kullanici savedKullanici = kullaniciRepository.save(kullanici);
        return convertKullaniciToDto(savedKullanici);
    }

    @Override
    public KullaniciDto update(Long kullaniciId, KullaniciDto kullaniciDto) {
        Kullanici kullanici = kullaniciRepository.findById(kullaniciId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Kullanıcı bilgilerini güncelle
        kullanici.setAd(kullaniciDto.getAd());
        kullanici.setSoyad(kullaniciDto.getSoyad());
        kullanici.setEmail(kullaniciDto.getEmail());
        kullanici.setTelefon(kullaniciDto.getTelefon());
        kullanici.setRol(kullaniciDto.getRol());
        kullanici.setCinsiyet(kullaniciDto.getCinsiyet());

        // Şifre güncelleme kontrolü
        if (kullaniciDto.getSifre() != null && !kullaniciDto.getSifre().isBlank()) {
            String hashedPassword = passwordEncoder.encode(kullaniciDto.getSifre());
            kullanici.setSifre(hashedPassword);
        }

        Kullanici updatedKullanici = kullaniciRepository.save(kullanici);
        return convertKullaniciToDto(updatedKullanici);
    }

    @Override
    public void delete(Long id) {
        kullaniciRepository.deleteById(id);
    }

    @Override
    public List<KullaniciDto> getAll() {
        return kullaniciRepository.findAll()
                .stream()
                .map(this::convertKullaniciToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<KullaniciDto> getAll(Pageable pageable) {
        return kullaniciRepository.findAll(pageable).map(this::convertKullaniciToDto);
    }

    private KullaniciDto convertKullaniciToDto(Kullanici kullanici) {
        KullaniciDto kullaniciDto = new KullaniciDto();
        kullaniciDto.setKullaniciId(kullanici.getKullaniciId());
        kullaniciDto.setAd(kullanici.getAd());
        kullaniciDto.setSoyad(kullanici.getSoyad());
        kullaniciDto.setEmail(kullanici.getEmail());
        kullaniciDto.setTelefon(kullanici.getTelefon());
        kullaniciDto.setRol(kullanici.getRol());
        kullaniciDto.setCinsiyet(kullanici.getCinsiyet());
        return kullaniciDto;
    }
}
