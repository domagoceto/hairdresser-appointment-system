package org.appointment.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

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
}

