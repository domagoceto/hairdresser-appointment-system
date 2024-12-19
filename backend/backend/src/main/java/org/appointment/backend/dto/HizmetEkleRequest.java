package org.appointment.backend.dto;

import lombok.Data;

@Data
public class HizmetEkleRequest {
    private String ad;         // Hizmet adı
    private String aciklama;   // Hizmet açıklaması
    private Integer sure;      // Hizmet süresi (dakika)
    private Double fiyat;      // Hizmet fiyatı
}

