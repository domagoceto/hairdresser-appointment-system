package org.appointment.backend.service;

import org.appointment.backend.dto.HizmetDto;
import org.appointment.backend.dto.HizmetEkleRequest;
import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.entity.Hizmet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HizmetService {
    Hizmet getHizmetById(Long id);
    HizmetDto hizmetEkle(HizmetDto hizmetDto);
    void hizmetSil(Long id); // Silme metodu tanımlandı
    List<Hizmet> getAllHizmetler();
}

