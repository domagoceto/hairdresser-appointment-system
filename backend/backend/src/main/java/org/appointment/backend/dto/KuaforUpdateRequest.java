package org.appointment.backend.dto;

import lombok.Data;

@Data
public class KuaforUpdateRequest {
    private String ad;
    private String soyad;
    private String telefon;
    private String email;
    private String sifre;  // Şifre opsiyonel olabilir
}
