package org.appointment.backend.service;

import org.appointment.backend.dto.RandevuDto;

import java.util.List;

public interface RandevuService {

    RandevuDto alRandevu(RandevuDto randevuDto);

    RandevuDto getRandevu(Long randevuId);

    List<RandevuDto> getAllRandevular();

    List<RandevuDto> getRandevularByKuaforId(Long kuaforId);


    List<RandevuDto> getPastRandevular(Long kullaniciId);

    List<RandevuDto> getFutureRandevular(Long kullaniciId);

    public List<RandevuDto> getRandevularByKullaniciId(Long kullaniciId);

    RandevuDto guncelleRandevu(Long randevuId, RandevuDto randevuDto, Long kullaniciId);

    void iptalRandevu(Long randevuId, Long kullaniciId);






}
