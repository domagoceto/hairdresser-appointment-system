package org.appointment.backend.service;

import org.appointment.backend.dto.HizmetDto;
import org.appointment.backend.dto.KullaniciDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HizmetService {
    List<HizmetDto> getAll();
    Page<KullaniciDto> getAll(Pageable pageable);

    HizmetDto getHizmetById(Long id);

    HizmetDto save(HizmetDto hizmetDto);

    HizmetDto update(Long id, HizmetDto hizmetDto);

    void delete(Long id);
}

