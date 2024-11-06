package org.appointment.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
public class Odeme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long odemeId;

    @Column(name = "tutar")
    private double tutar;

    @Column(name="odeme_tarihi")
    private LocalDateTime odemeTarihi;

    @Enumerated(EnumType.STRING)
    @Column(name="durum",length = 20)
    private OdemeDurum durum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id")
    private Kullanici kullanici;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="randevu_id")
    private Randevu randevu;

    @Enumerated(EnumType.STRING)
    @Column(name="odeme_yontemi",length = 50)
    private OdemeYontemi odemeYontemi;

    @Column(name = "aciklama", length = 500)
    private String aciklama;

}
