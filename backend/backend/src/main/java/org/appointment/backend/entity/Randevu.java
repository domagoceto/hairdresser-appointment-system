package org.appointment.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Table(name = "randevular")
@Data
@Entity

public class Randevu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long randevuId;

    @Column(length = 50, name="tarih")
    private LocalDate tarih;

    @Column(length = 50, name = "saat")
    private LocalTime saat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="kuafor_id")
    private Kuafor kuafor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hizmet_id")
    private Hizmet hizmet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kullanici_id")
    private Kullanici kullanici;


    @Enumerated(EnumType.STRING)
    @Column(length = 20,name = "durum")
    private RandevuDurum durum;

    @Column(length = 300, name = "notlar")
    private String notlar;


    @Column(name = "ucret")
    private double ucret;

    @Column(name = "sure")
    private String sure; // işlem süresi dakika cinsinden

    @Column(name = "created_at") //Randevu oluşturulma tarihi
    private LocalDateTime createdAt;

    @Column(name = "updated_at") //Randevu güncelleme tarihi
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
