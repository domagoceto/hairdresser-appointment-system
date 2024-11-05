package org.appointment.backend.dto;

import lombok.Data;
import org.appointment.backend.entity.Cinsiyet;
import org.appointment.backend.entity.Rol;

@Data

public class KullaniciDto {

    private Long id;
    private String ad;
    private String soyad;
    private Cinsiyet cinsiyet;
    private String email;
    private String telefon;
    private String sifre;
    private Rol rol;
}
