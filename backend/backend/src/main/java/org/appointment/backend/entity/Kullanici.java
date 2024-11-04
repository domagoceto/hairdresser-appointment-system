package org.appointment.backend.entity;

import jakarta.persistence.*;



import lombok.Getter;
import lombok.Setter;

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


}
