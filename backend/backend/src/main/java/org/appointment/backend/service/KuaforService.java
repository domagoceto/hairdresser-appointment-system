package org.appointment.backend.service;

import org.appointment.backend.dto.KuaforDetailsResponse;
import org.appointment.backend.dto.KuaforRegisterRequest;
import org.appointment.backend.dto.KuaforUpdateRequest;
import org.appointment.backend.entity.Kuafor;
import org.appointment.backend.dto.RandevuDto;

import java.util.List;

public interface KuaforService {
    Kuafor registerKuafor(KuaforRegisterRequest request);
    KuaforDetailsResponse getKuaforDetails(Long kuaforId, String currentUserEmail);  // Parametre uyumu sağlanıyor
    Kuafor addServiceToKuafor(Long kuaforId, Long hizmetId);
    Kuafor updateKuaforInfo(Long kuaforId, KuaforUpdateRequest updateRequest, String currentUserEmail);  // Parametre uyumu sağlanıyor
    Kuafor getKuaforById(Long id);
    List<RandevuDto> getKuaforRandevular(String currentUserEmail);  // Kuaförün randevularını almak için metod
    Kuafor save(Kuafor kuafor);
}
