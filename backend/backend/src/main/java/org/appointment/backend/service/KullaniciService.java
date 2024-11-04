package org.appointment.backend.service;

import org.appointment.backend.dto.KullaniciDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KullaniciService {
    KullaniciDto save(KullaniciDto kullaniciDto);

    void delete(Long id);

    List<KullaniciDto> getAll();

    Page<KullaniciDto> getAll(Pageable pageable);
}