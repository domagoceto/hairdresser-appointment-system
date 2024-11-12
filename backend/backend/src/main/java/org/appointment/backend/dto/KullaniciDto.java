package org.appointment.backend.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.appointment.backend.entity.Cinsiyet;
import org.appointment.backend.entity.Rol;

@Data
@JsonPropertyOrder({"kullaniciId", "ad", "soyad", "email", "telefon", "sifre", "cinsiyet","rol"})
public class KullaniciDto {

    private Long kullaniciId;
    private String ad;
    private String soyad;
    private Cinsiyet cinsiyet;
    private String email;
    private String telefon;
    private String sifre;
    private Rol rol;
}
