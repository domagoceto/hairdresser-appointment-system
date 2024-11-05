package org.appointment.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
public class Kullanici {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, name = "ad")
    private String ad;

    @Column(length = 100, name = "soyad")
    private String soyad;

    @Enumerated(EnumType.STRING)
    @Column(length = 10 ,name="cinsiyet" )
    private Cinsiyet cinsiyet;

    @Column(length = 15, name = "telefon")
    private String telefon;

    @Column (length = 100,name="email")
    private String email;

    @Column (length = 100,name="sifre")
    private String sifre;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, name = "rol")
    private Rol rol;
}


