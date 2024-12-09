package org.appointment.backend.dto;

import lombok.Data;

@Data
public class KullaniciUpdateRequest {
    private String ad;
    private String soyad;
    private String telefon;
    private String sifre; // Şifreyi güncellemek isteyebilir
    private String email; // E-posta değişikliği için
}
