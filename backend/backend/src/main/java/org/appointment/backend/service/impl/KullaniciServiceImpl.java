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

        kullaniciDto.setKullaniciId(kullaniciDto.getKullaniciId());
        return kullaniciDto;
    }

    @Override
    public void delete(Long id) {

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