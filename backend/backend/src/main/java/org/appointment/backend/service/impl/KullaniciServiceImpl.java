package org.appointment.backend.service.impl;

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
public class KullaniciServiceImpl implements KullaniciService {
    private final KullaniciRepository kullaniciRepository;

    public KullaniciServiceImpl(KullaniciRepository kullaniciRepository) {
        this.kullaniciRepository = kullaniciRepository;
    }

    @Override
    @Transactional
    public KullaniciDto save(KullaniciDto kullaniciDto) {
        Kullanici kullanici = new Kullanici();
        kullanici.setAd(kullaniciDto.getAd());
        kullanici.setSoyad(kullaniciDto.getSoyad());

        Kullanici savedKullanici = kullaniciRepository.save(kullanici);
        kullaniciDto.setId(savedKullanici.getId());
        return kullaniciDto;
    }

    @Override
    public void delete(Long id) {
        kullaniciRepository.deleteById(id);
    }

    @Override
    public List<KullaniciDto> getAll() {
        List<Kullanici> kullanicilar = kullaniciRepository.findAll();
        List<KullaniciDto> kullaniciDtos = new ArrayList<>();

        kullanicilar.forEach(it -> {
            KullaniciDto kullaniciDto = new KullaniciDto();
            kullaniciDto.setId(it.getId());
            kullaniciDto.setAd(it.getAd());
            kullaniciDto.setSoyad(it.getSoyad());
            kullaniciDtos.add(kullaniciDto);
        });

        return kullaniciDtos;
    }

    @Override
    public Page<KullaniciDto> getAll(Pageable pageable) {
        return kullaniciRepository.findAll(pageable).map(kullanici -> {
            KullaniciDto kullaniciDto = new KullaniciDto();
            kullaniciDto.setId(kullanici.getId());
            kullaniciDto.setAd(kullanici.getAd());
            kullaniciDto.setSoyad(kullanici.getSoyad());
            return kullaniciDto;
        });
    }
}
