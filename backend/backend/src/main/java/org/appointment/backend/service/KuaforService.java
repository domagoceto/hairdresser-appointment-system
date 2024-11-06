package org.appointment.backend.service;

import org.appointment.backend.dto.KuaforDto;

import java.util.List;

public interface KuaforService {
    List<KuaforDto> getAllKuaforler();
    KuaforDto getKuaforById(Long id);
    KuaforDto createKuafor(KuaforDto kuaforDto);
    KuaforDto updateKuafor(Long id, KuaforDto kuaforDto);
    void deleteKuafor(Long id);
}