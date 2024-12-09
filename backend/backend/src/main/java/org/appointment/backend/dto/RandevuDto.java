package org.appointment.backend.dto;

import lombok.Data;
import org.appointment.backend.entity.Hizmet;
import org.appointment.backend.entity.Kuafor;
import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.entity.RandevuDurum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
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
}
