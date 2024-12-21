package org.appointment.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminKullaniciDto {
    private String ad;
    private String soyad;
    private String cinsiyet; // Enum ise String'e çeviriyoruz
    private String telefon;
    private String email;
    private String rol;
}
