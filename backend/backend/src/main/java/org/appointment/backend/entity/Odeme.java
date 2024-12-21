package org.appointment.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "odemeler")
@Getter
@Setter
@Entity
public class Odeme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long odemeId;

    @Column(name = "tutar", nullable = false)
    private double tutar;

    @Column(name = "odeme_tarihi", nullable = false)
    private LocalDateTime odemeTarihi;

    @Enumerated(EnumType.STRING)
    @Column(name = "durum", length = 20, nullable = false)
    private OdemeDurum durum;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "randevu_id", nullable = false)
    private Randevu randevu;

    @Enumerated(EnumType.STRING)
    @Column(name = "odeme_yontemi", length = 50)
    private OdemeYontemi odemeYontemi;

    @Column(name = "aciklama", length = 500)
    private String aciklama;
}
