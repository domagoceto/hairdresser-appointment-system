package org.appointment.backend.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RandevuResponse {
    private Long randevuId;
    private String kuaforAd;
    private String kuaforSoyad;
    private String musteriAd; // Kullanıcı adı ekleniyor
    private String musteriSoyad; // Kullanıcı soyadı ekleniyor
    private String hizmetAdi;
    private LocalDate tarih;
    private LocalTime saat;
    private String durum;
    private Double ucret;
    private String notlar;
    private String sure;
    private String hizmet;

}





