package org.appointment.backend.service;

import org.appointment.backend.dto.HizmetDto;

import java.util.List;

public interface HizmetService {
    List<HizmetDto> tumunuListele();
    HizmetDto getHizmetById(Long id);
    HizmetDto kaydet(HizmetDto hizmetDto);
    HizmetDto guncelle(Long id, HizmetDto hizmetDto);
    void sil(Long id);
}