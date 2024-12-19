package org.appointment.backend.dto;

import lombok.Data;

@Data
public class KuaforDetailsResponse {
    private Long kuaforId;
    private String ad;
    private String soyad;
    private String email;
    private String telefon;
    private String cinsiyet;

    // Parametreli Constructor
    public KuaforDetailsResponse(Long kuaforId, String ad, String soyad, String email, String telefon, String cinsiyet) {
        this.kuaforId = kuaforId;
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.telefon = telefon;
        this.cinsiyet = cinsiyet;
    }
}
