package org.appointment.backend.dto;

import lombok.Builder;
import lombok.Data;
import org.appointment.backend.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Data
@Builder
public class RandevuDto {
    private Long randevuId;
    private LocalDate tarih;
    private LocalTime saat;
    private Long kuaforId;
    private Long hizmetId;
    private Long kullaniciId;
    private RandevuDurum durum;
    private String notlar;
    private Double ucret;
    private String sure;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String hizmetAdi; // Yeni alan: Hizmet adı

    // Açık yapıcı
    public RandevuDto(Long randevuId, LocalDate tarih, LocalTime saat, Long kuaforId, Long hizmetId, Long kullaniciId,
                      RandevuDurum durum, String notlar, Double ucret, String sure,
                      LocalDateTime createdAt, LocalDateTime updatedAt, String hizmetAdi) {
        this.randevuId = randevuId;
        this.tarih = tarih;
        this.saat = saat;
        this.kuaforId = kuaforId;
        this.hizmetId = hizmetId;
        this.kullaniciId = kullaniciId;
        this.durum = durum;
        this.notlar = notlar;
        this.ucret = ucret;
        this.sure = sure;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.hizmetAdi = hizmetAdi;
    }

}

