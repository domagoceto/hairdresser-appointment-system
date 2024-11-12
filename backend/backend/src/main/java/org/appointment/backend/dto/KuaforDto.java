package org.appointment.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.appointment.backend.entity.Cinsiyet;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KuaforDto {
    private Long kuaforId;
    private String ad;
    private String soyad;
    private Cinsiyet cinsiyet;
    private String telefon;
    private String email;
    private List<Long> yapabilecegiHizmetlerIds;
}