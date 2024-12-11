package org.appointment.backend.service.impl;

import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.entity.Rol;
import org.appointment.backend.repo.KullaniciRepository;
import org.appointment.backend.service.KullaniciService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KullaniciServiceImpl implements KullaniciService {

    private final KullaniciRepository kullaniciRepository;
    private final PasswordEncoder passwordEncoder;

    public KullaniciServiceImpl(KullaniciRepository kullaniciRepository, PasswordEncoder passwordEncoder) {
        this.kullaniciRepository = kullaniciRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public KullaniciDto save(KullaniciDto kullaniciDto) {
        log.info("Kaydedilecek kullanıcı bilgileri: {}", kullaniciDto);

        // Rol kontrolü
        if (kullaniciDto.getRol() == null) {
            log.warn("Kullanıcının rolü null, varsayılan olarak MUSTERI atanıyor.");
            kullaniciDto.setRol(Rol.MUSTERI); // Varsayılan değer atanıyor
        }

        Kullanici kullanici = new Kullanici();
        kullanici.setAd(kullaniciDto.getAd());
        kullanici.setSoyad(kullaniciDto.getSoyad());
        kullanici.setEmail(kullaniciDto.getEmail());
        kullanici.setTelefon(kullaniciDto.getTelefon());
        kullanici.setCinsiyet(kullaniciDto.getCinsiyet());

        // Şifre hashleme
        String hashedPassword = passwordEncoder.encode(kullaniciDto.getSifre());
        log.info("Hashlenmiş şifre: {}", hashedPassword);
        kullanici.setSifre(hashedPassword);

        // Rol atama
        kullanici.setRol(kullaniciDto.getRol());

        Kullanici savedKullanici = kullaniciRepository.save(kullanici);
        log.info("Veritabanına kaydedilen kullanıcı: {}", savedKullanici);

        return convertToDto(savedKullanici);
    }



    @Override
    public KullaniciDto update(Long kullaniciId, KullaniciDto kullaniciDto) {
        Kullanici kullanici = kullaniciRepository.findById(kullaniciId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        kullanici.setAd(kullaniciDto.getAd());
        kullanici.setSoyad(kullaniciDto.getSoyad());
        kullanici.setTelefon(kullaniciDto.getTelefon());
        kullanici.setEmail(kullaniciDto.getEmail());
        if (kullaniciDto.getSifre() != null) {
            kullanici.setSifre(passwordEncoder.encode(kullaniciDto.getSifre()));
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
    public KullaniciDto updateByEmail(String email, KullaniciDto kullaniciDto) {
        Kullanici kullanici = kullaniciRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        kullanici.setAd(kullaniciDto.getAd());
        kullanici.setSoyad(kullaniciDto.getSoyad());
        kullanici.setTelefon(kullaniciDto.getTelefon());
        kullanici.setEmail(kullaniciDto.getEmail());
        if (kullaniciDto.getSifre() != null) {
            kullanici.setSifre(passwordEncoder.encode(kullaniciDto.getSifre()));
        }
        Kullanici updatedKullanici = kullaniciRepository.save(kullanici);
        return convertToDto(updatedKullanici);
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
