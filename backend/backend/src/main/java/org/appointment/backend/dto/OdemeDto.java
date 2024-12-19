package org.appointment.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OdemeDto {
    private Long odemeId;
    private Double tutar; // Randevudan otomatik çekilecek, manuel girilmeyecek
    private LocalDateTime odemeTarihi;
    private String durum;
    private Long kullaniciId; // Kullanıcı randevudan alınacak
    private String adSoyad; // Kullanıcı adı ve soyadı
    private Long randevuId;
    private String odemeYontemi;
    private String aciklama;
}
