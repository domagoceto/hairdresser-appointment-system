package org.appointment.backend.service;

import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.entity.Kullanici;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface KullaniciService {

    KullaniciDto save(KullaniciDto kullaniciDto);

    KullaniciDto update(Long kullaniciId, KullaniciDto kullaniciDto);

    void delete(Long id);

    List<KullaniciDto> getAll();

    Page<KullaniciDto> getAll(Pageable pageable);

    KullaniciDto updateByEmail(String email, KullaniciDto kullaniciDto);

    void deleteByEmail(String email);

    KullaniciDto findByEmail(String email);

    public Kullanici findEntityByEmail(String email);

}
