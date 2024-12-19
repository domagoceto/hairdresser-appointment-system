package org.appointment.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KuaforRegisterRequest {
    private String ad;
    private String soyad;
    private String email;
    private String telefon;
    private String sifre;
    private String cinsiyet;
    private String kuaforKey;// Enum: ERKEK veya KADIN
}