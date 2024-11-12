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
    private Kuafor kuafor;
    private Hizmet hizmet ;
    private Kullanici kullanici;
    private RandevuDurum durum;
    private String notlar;
    private double ucret;
    private int sure;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
