package org.appointment.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OdemeDto {
    private Long odemeId;          // Ödeme ID'si
    private Double tutar;          // Ücret bilgisi
    private LocalDateTime odemeTarihi; // Ödeme tarihi
    private String durum;          // Ödeme durumu (ODENDI, ODENMEDI, BEKLIYOR)
    private Long kullaniciId;      // Kullanıcı ID'si
    private String adSoyad;        // Kullanıcı adı ve soyadı
    private Long randevuId;        // Randevu ID'si
    private String odemeYontemi;   // Ödeme yöntemi
    private String aciklama;
    private String islem;


    // Ekstra constructor: Basit veri çekim için
    public OdemeDto(String adSoyad, String islem, Double tutar) {
        this.adSoyad = adSoyad;
        this.aciklama = aciklama; // "islem" bilgisi açıklama olarak atanıyor
        this.tutar = tutar;
    }
}
