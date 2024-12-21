package org.appointment.backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.HizmetDto;
import org.appointment.backend.dto.HizmetEkleRequest;
import org.appointment.backend.entity.Hizmet;
import org.appointment.backend.entity.Kuafor;
import org.appointment.backend.entity.Randevu;
import org.appointment.backend.repo.HizmetRepository;
import org.appointment.backend.repo.KuaforRepository;
import org.appointment.backend.repo.RandevuRepository;
import org.appointment.backend.service.HizmetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HizmetServiceImpl implements HizmetService {

    private final HizmetRepository hizmetRepository;
    private final RandevuRepository randevuRepository;
    private final KuaforRepository kuaforRepository;

    @Override
    public HizmetDto hizmetEkle(HizmetDto hizmetDto) {
        Hizmet hizmet = new Hizmet();
        hizmet.setAd(hizmetDto.getAd());
        hizmet.setAciklama(hizmetDto.getAciklama());
        hizmet.setFiyat(hizmetDto.getFiyat());
        hizmet.setSure(hizmetDto.getSure());

        Hizmet savedHizmet = hizmetRepository.save(hizmet);
        return toDto(savedHizmet);
    }

    private HizmetDto toDto(Hizmet hizmet) {
        return HizmetDto.builder()
                .hizmetId(hizmet.getHizmetId())
                .ad(hizmet.getAd())
                .aciklama(hizmet.getAciklama())
                .fiyat(hizmet.getFiyat())
                .sure(hizmet.getSure())
                .build();
    }

    @Override
    public Hizmet getHizmetById(Long id) {
        return hizmetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hizmet bulunamadı: " + id));
    }

    @Override
    public void hizmetSil(Long id) {
        if (hizmetRepository.existsById(id)) {
            hizmetRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Hizmet bulunamadı. ID: " + id);
        }
    }

    @Override
    public List<Hizmet> getAllHizmetler() {
        return hizmetRepository.findAll();
    }

}

