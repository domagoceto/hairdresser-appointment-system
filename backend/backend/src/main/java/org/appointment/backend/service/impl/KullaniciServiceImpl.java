package org.appointment.backend.service.impl;

import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.repo.KullaniciRepository;
import org.appointment.backend.service.KullaniciService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
import lombok.extern.slf4j.Slf4j;
=======
>>>>>>> parent of b67c09f (kullanıcı kayıt,giris güncellendi)

import java.util.List;
import java.util.stream.Collectors;

@Service
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
        kullanici.setAd(kullaniciDto.getAd());
        kullanici.setSoyad(kullaniciDto.getSoyad());
        kullanici.setEmail(kullaniciDto.getEmail());
        kullanici.setTelefon(kullaniciDto.getTelefon());
        kullanici.setSifre(passwordEncoder.encode(kullaniciDto.getSifre()));
        kullanici.setRol(kullaniciDto.getRol());
        kullanici.setCinsiyet(kullaniciDto.getCinsiyet());

        Kullanici savedKullanici = kullaniciRepository.save(kullanici);
        return convertToDto(savedKullanici);
    }

    @Override
    public KullaniciDto update(Long kullaniciId, KullaniciDto kullaniciDto) {
        Kullanici kullanici = kullaniciRepository.findById(kullaniciId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        kullanici.setAd(kullaniciDto.getAd() != null ? kullaniciDto.getAd() : kullanici.getAd());
        kullanici.setSoyad(kullaniciDto.getSoyad() != null ? kullaniciDto.getSoyad() : kullanici.getSoyad());
        kullanici.setTelefon(kullaniciDto.getTelefon() != null ? kullaniciDto.getTelefon() : kullanici.getTelefon());
        kullanici.setEmail(kullaniciDto.getEmail() != null ? kullaniciDto.getEmail() : kullanici.getEmail());

        // Şifre güncelleniyorsa, hash'le ve logla
        if (kullaniciDto.getSifre() != null && !kullaniciDto.getSifre().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(kullaniciDto.getSifre());
            log.info("Hashlenmiş şifre (güncelleme): {}", hashedPassword);
            kullanici.setSifre(hashedPassword);
        }

        Kullanici updatedKullanici = kullaniciRepository.save(kullanici);
        return convertToDto(updatedKullanici);
    }

    @Override
    public KullaniciDto updateByEmail(String email, KullaniciDto kullaniciDto) {
        Kullanici kullanici = kullaniciRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        kullanici.setAd(kullaniciDto.getAd() != null ? kullaniciDto.getAd() : kullanici.getAd());
        kullanici.setSoyad(kullaniciDto.getSoyad() != null ? kullaniciDto.getSoyad() : kullanici.getSoyad());
        kullanici.setTelefon(kullaniciDto.getTelefon() != null ? kullaniciDto.getTelefon() : kullanici.getTelefon());
        kullanici.setEmail(kullaniciDto.getEmail() != null ? kullaniciDto.getEmail() : kullanici.getEmail());

        // Şifre güncelleniyorsa, hash'le ve logla
        if (kullaniciDto.getSifre() != null && !kullaniciDto.getSifre().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(kullaniciDto.getSifre());
            log.info("Hashlenmiş şifre (güncelleme): {}", hashedPassword);
            kullanici.setSifre(hashedPassword);
        }

        Kullanici updatedKullanici = kullaniciRepository.save(kullanici);
        return convertToDto(updatedKullanici);
    }


    @Override
    public void delete(Long id) {
        kullaniciRepository.deleteById(id);
    }

    @Override
    public List<KullaniciDto> getAll() {
        return kullaniciRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<KullaniciDto> getAll(Pageable pageable) {
        return kullaniciRepository.findAll(pageable).map(this::convertToDto);
    }

    @Override
    public KullaniciDto findByEmail(String email) {
        Kullanici kullanici = kullaniciRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return convertToDto(kullanici);
    }

    @Override
    public Kullanici findEntityByEmail(String email) {
        return kullaniciRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }



    @Override
    public void deleteByEmail(String email) {
        Kullanici kullanici = kullaniciRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        kullaniciRepository.delete(kullanici);
    }

    private KullaniciDto convertToDto(Kullanici kullanici) {
        KullaniciDto dto = new KullaniciDto();
        dto.setKullaniciId(kullanici.getKullaniciId());
        dto.setAd(kullanici.getAd());
        dto.setSoyad(kullanici.getSoyad());
        dto.setEmail(kullanici.getEmail());
        dto.setTelefon(kullanici.getTelefon());
        dto.setRol(kullanici.getRol());
        dto.setCinsiyet(kullanici.getCinsiyet());
        return dto;
    }
}
