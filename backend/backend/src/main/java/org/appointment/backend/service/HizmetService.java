package org.appointment.backend.service;

import org.appointment.backend.dto.HizmetDto;

import java.util.List;

public interface HizmetService {
    List<HizmetDto> getAllHizmetler();
    HizmetDto getHizmetById(Long id);
    HizmetDto createHizmet(HizmetDto hizmetDto);
    HizmetDto updateHizmet(Long id, HizmetDto hizmetDto);
    void deleteHizmet(Long id);
}