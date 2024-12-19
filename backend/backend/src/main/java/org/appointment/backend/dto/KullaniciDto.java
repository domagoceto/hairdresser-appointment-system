package org.appointment.backend.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.appointment.backend.entity.Cinsiyet;
import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.entity.Rol;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"kullaniciId", "ad", "soyad", "email", "telefon", "sifre", "cinsiyet", "rol", "kuaforKey"})
public class KullaniciDto {

    private Long kullaniciId;
    private String ad;
    private String soyad;
    private Cinsiyet cinsiyet;
    private String email;
    private String telefon;
    private String sifre;
    private Rol rol;
    private String kuaforKey; // Yeni alan

    public static KullaniciDto fromEntity(Kullanici kullanici) {
        return KullaniciDto.builder()
                .kullaniciId(kullanici.getKullaniciId())
                .ad(kullanici.getAd())
                .soyad(kullanici.getSoyad())
                .email(kullanici.getEmail())
                .telefon(kullanici.getTelefon())
                .cinsiyet(kullanici.getCinsiyet())
                .rol(kullanici.getRol())
                .build();
    }
}
