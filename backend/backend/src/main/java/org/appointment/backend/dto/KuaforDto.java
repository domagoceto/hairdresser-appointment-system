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
public class KuaforDto {
    private Long kuaforId;
    private String ad;
    private String soyad;
    private String cinsiyet;
    private String telefon;
    private String email;
    private List<Long> yapabilecegiHizmetlerIds;
}