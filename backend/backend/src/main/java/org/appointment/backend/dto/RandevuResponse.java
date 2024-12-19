package org.appointment.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class RandevuResponse {
    private Long randevuId;
    private String kuaforAd;
    private String kuaforSoyad;
    private String musteriAd;
    private String musteriSoyad;
    private String hizmetAdi;
    private LocalDate tarih;
    private LocalTime saat;
    private String durum; // RandevuDurum enum string hali
    private double ucret;
    private String notlar;
    private String sure;
}
