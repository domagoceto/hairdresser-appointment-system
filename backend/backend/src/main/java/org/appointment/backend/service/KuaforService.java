package org.appointment.backend.service;

import org.appointment.backend.dto.KuaforDto;

import java.util.List;

public interface KuaforService {
    List<KuaforDto> tumunuListele();
    KuaforDto getKuaforById(Long id);
    KuaforDto kaydet(KuaforDto kuaforDto);
    KuaforDto guncelle(Long id, KuaforDto kuaforDto);
    void sil(Long id);
}