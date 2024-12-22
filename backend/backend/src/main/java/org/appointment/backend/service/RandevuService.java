package org.appointment.backend.service;

import org.appointment.backend.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface RandevuService {

    RandevuDto alRandevu(RandevuDto randevuDto);

    RandevuDto getRandevu(Long randevuId);

    List<RandevuDto> getAllRandevular();

    List<RandevuDto> getRandevularByKuaforId(Long kuaforId);


    List<RandevuDto> getRandevularByKullaniciId(Long kullaniciId);


    RandevuDto guncelleRandevu(Long randevuId, RandevuDto randevuDto, Long kullaniciId);

    void iptalRandevu(Long randevuId, Long kullaniciId);

    List<KuaforRandevuDto> getRandevularByKuaforAndTarih(Long kuaforId, LocalDate tarih);

    List<KuaforRandevuResponseDto> getKuaforRandevular(String email, Long kuaforId, LocalDate tarih);

    public RandevuResponse createRandevu(RandevuRequest request, Long kullaniciId);










}
