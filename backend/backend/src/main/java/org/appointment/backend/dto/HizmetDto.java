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
    private List<Long> randevuIds;    // Randevuların ID'leri
    private List<Long> kuaforIds;     // Kuaforlerin ID'leri
}

