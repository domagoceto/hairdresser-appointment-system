package org.appointment.backend.dto;

import lombok.Data;
import org.appointment.backend.entity.RandevuDurum;

import java.time.LocalTime;

@Data
public class KuaforRandevuDto {
    private LocalTime saat;
    private String adSoyad;
    private String telefon;
    private String islem;
    private RandevuDurum durum;
}
