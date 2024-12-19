package org.appointment.backend.dto;

import lombok.Data;

@Data
public class KuaforRandevuResponseDto {
    private String adSoyad; // Kullanıcı adı ve soyadı
    private String hizmet;  // Hizmet adı
    private String saat;    // Saat bilgisi
    private String notlar;  // Randevu notları
    private double ucret;   // Ücret bilgisi
    private String sure;    // Süre bilgisi
}
