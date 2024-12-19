package org.appointment.backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String ad;
    private String soyad;
    private String email;
    private String telefon;
    private String sifre;
    private String cinsiyet;   // Kullanıcı cinsiyeti (ERKEK, KADIN vb.)
    private String adminKey;   // Admin anahtarı
    private String kuaforKey;
}

