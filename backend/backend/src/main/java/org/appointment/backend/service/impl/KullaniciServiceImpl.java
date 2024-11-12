package org.appointment.backend.service.impl;

import lombok.*;
import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.repo.KullaniciRepository;
import org.appointment.backend.service.KullaniciService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class KullaniciServiceImpl implements KullaniciService {

    private final KullaniciRepository kullaniciRepository;

    @Override
    @Transactional
    public KullaniciDto save(KullaniciDto kullaniciDto) {
        Kullanici kullanici = new Kullanici();
        kullanici.setAd(kullaniciDto.getAd());
        kullanici.setSoyad(kullaniciDto.getSoyad());
        kullanici.setRol(kullaniciDto.getRol());
        kullanici.setEmail(kullaniciDto.getEmail());
        kullanici.setTelefon(kullaniciDto.getTelefon());
        kullanici.setSifre(kullaniciDto.getSifre());
        kullanici.setCinsiyet(kullaniciDto.getCinsiyet());
        final Kullanici kullanicidb = kullaniciRepository.save(kullanici);

        kullaniciDto.setKullaniciId(kullanicidb.getKullaniciId());
        return kullaniciDto;
    }

    @Override
    public void delete(Long id) {
        kullaniciRepository.deleteById(id);

    }

    @Transactional
    @Override
    public KullaniciDto update(Long kullaniciId, KullaniciDto kullaniciDto) {
        // Mevcut kullanıcıyı veritabanından bul
        Kullanici kullanici = kullaniciRepository.findById(kullaniciId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Sadece gelen kullaniciDto'da dolu olan alanları güncelle
        kullanici.setAd(kullaniciDto.getAd() != null ? kullaniciDto.getAd() : kullanici.getAd());
        kullanici.setSoyad(kullaniciDto.getSoyad() != null ? kullaniciDto.getSoyad() : kullanici.getSoyad());
        kullanici.setRol(kullaniciDto.getRol() != null ? kullaniciDto.getRol() : kullanici.getRol());
        kullanici.setEmail(kullaniciDto.getEmail() != null ? kullaniciDto.getEmail() : kullanici.getEmail());
        kullanici.setTelefon(kullaniciDto.getTelefon() != null ? kullaniciDto.getTelefon() : kullanici.getTelefon());
        kullanici.setSifre(kullaniciDto.getSifre() != null ? kullaniciDto.getSifre() : kullanici.getSifre());
        kullanici.setCinsiyet(kullaniciDto.getCinsiyet() != null ? kullaniciDto.getCinsiyet() : kullanici.getCinsiyet());

        // Güncellenmiş kullanıcıyı kaydet
        Kullanici updatedKullanici = kullaniciRepository.save(kullanici);

        // Güncellenmiş bilgileri geri döndür
        KullaniciDto updatedKullaniciDto = new KullaniciDto();
        updatedKullaniciDto.setKullaniciId(updatedKullanici.getKullaniciId());
        updatedKullaniciDto.setAd(updatedKullanici.getAd());
        updatedKullaniciDto.setSoyad(updatedKullanici.getSoyad());
        updatedKullaniciDto.setRol(updatedKullanici.getRol());
        updatedKullaniciDto.setEmail(updatedKullanici.getEmail());
        updatedKullaniciDto.setTelefon(updatedKullanici.getTelefon());
        updatedKullaniciDto.setSifre(updatedKullanici.getSifre());
        updatedKullaniciDto.setCinsiyet(updatedKullanici.getCinsiyet());

        return updatedKullaniciDto;
    }



    @Override
    public List<KullaniciDto> getAll() {
        List<Kullanici> kullanicilar = kullaniciRepository.findAll();
        List<KullaniciDto> kullaniciDtos = new ArrayList<>();

        kullanicilar.forEach(it ->{
            KullaniciDto kullaniciDto = new KullaniciDto();
            kullaniciDto.setKullaniciId(it.getKullaniciId());
            kullaniciDto.setAd(it.getAd());
            kullaniciDto.setSoyad(it.getSoyad());
            kullaniciDto.setRol(it.getRol());
            kullaniciDto.setEmail(it.getEmail());
            kullaniciDto.setTelefon(it.getTelefon());
            kullaniciDto.setSifre(it.getSifre());
            kullaniciDto.setCinsiyet(it.getCinsiyet());
            kullaniciDtos.add(kullaniciDto);

        });
        return kullaniciDtos;
    }
    @Override
    public Page<KullaniciDto> getAll(Pageable pageable) {
        return null;
    }


}