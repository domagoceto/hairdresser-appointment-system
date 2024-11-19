package org.appointment.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.HizmetDto;
import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.entity.Hizmet;
import org.appointment.backend.repo.HizmetRepository;
import org.appointment.backend.service.HizmetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HizmetServiceImpl implements HizmetService {

    private final HizmetRepository hizmetRepository;

    @Override
    public List<HizmetDto> getAll() {
        return hizmetRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Page<KullaniciDto> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public HizmetDto getHizmetById(Long id) {
        return hizmetRepository.findById(id).map(this::convertToDto).orElseThrow(() -> new RuntimeException("Hizmet not found"));
    }

    @Override
    public HizmetDto save(HizmetDto hizmetDto) {
        Hizmet hizmet = convertToEntity(hizmetDto);
        return convertToDto(hizmetRepository.save(hizmet));
    }

    @Override
    public HizmetDto update(Long id, HizmetDto hizmetDto) {
        Hizmet existingHizmet = hizmetRepository.findById(id).orElseThrow(() -> new RuntimeException("Hizmet bulunamadı"));
        existingHizmet.setAd(hizmetDto.getAd());
        existingHizmet.setAciklama(hizmetDto.getAciklama());
        existingHizmet.setSure(hizmetDto.getSure());
        existingHizmet.setFiyat(hizmetDto.getFiyat());
        return convertToDto(hizmetRepository.save(existingHizmet));
    }

    @Override
    public void delete(Long id) {
        hizmetRepository.deleteById(id);
    }

    private HizmetDto convertToDto(Hizmet hizmet) {
        return HizmetDto.builder()
                .hizmetId(hizmet.getHizmetId())
                .ad(hizmet.getAd())
                .aciklama(hizmet.getAciklama())
                .sure(hizmet.getSure())
                .fiyat(hizmet.getFiyat())
                .build();
    }

    private Hizmet convertToEntity(HizmetDto hizmetDto) {
        return Hizmet.builder()
                .hizmetId(hizmetDto.getHizmetId())
                .ad(hizmetDto.getAd())
                .aciklama(hizmetDto.getAciklama())
                .sure(hizmetDto.getSure())
                .fiyat(hizmetDto.getFiyat())
                .build();
    }
}

