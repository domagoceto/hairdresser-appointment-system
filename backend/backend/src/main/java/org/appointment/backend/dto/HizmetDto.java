package org.appointment.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HizmetDto {
    private Long hizmetId;
    private String ad;
    private String aciklama;
    private Integer sure;
    private Double fiyat;
    private String imageUrl;
    private List<Long> randevuIds;    // Randevuların ID'leri
    private List<Long> kuaforIds;// Kuaforlerin ID'leri

    public HizmetDto(Long hizmetId, String ad, String aciklama, Double fiyat, Integer sure,String imageUrl) {
        this.hizmetId = hizmetId;
        this.ad = ad;
        this.aciklama = aciklama;
        this.fiyat = fiyat;
        this.sure = sure;
        this.imageUrl = imageUrl;
    }


}

