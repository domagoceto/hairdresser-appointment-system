package org.appointment.backend.service;

import org.appointment.backend.dto.KuaforDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KuaforService {
    List<KuaforDto> getAll();  // tumunuListele -> getAll olarak değiştirildi
    Page<KuaforDto> getAll(Pageable pageable); // Sayfalama desteği eklendi
    KuaforDto save(KuaforDto kuaforDto); // kaydet -> save olarak değiştirildi
    KuaforDto update(Long id, KuaforDto kuaforDto); // guncelle -> update olarak değiştirildi
    void delete(Long id); // sil -> delete olarak değiştirildi
}
