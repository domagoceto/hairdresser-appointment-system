package org.appointment.backend.service;

import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.repo.KullaniciRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KullaniciService {
    private final KullaniciRepository kullaniciRepository;

    public KullaniciService(KullaniciRepository kullaniciRepository) {
        this.kullaniciRepository = kullaniciRepository;
    }

    public KullaniciDto save(KullaniciDto kullaniciDto) {
        Kullanici kullanici = new Kullanici();
        kullanici.setAd(kullaniciDto.getAd());
        kullanici.setSoyad(kullaniciDto.getSoyad());

        Kullanici savedKullanici = kullaniciRepository.save(kullanici);
        return convertToDto(savedKullanici);
    }

    public List<KullaniciDto> getAll() {
        return kullaniciRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private KullaniciDto convertToDto(Kullanici kullanici) {
        KullaniciDto dto = new KullaniciDto();
        dto.setId(kullanici.getId());
        dto.setAd(kullanici.getAd());
        dto.setSoyad(kullanici.getSoyad());
        return dto;
    }
}
