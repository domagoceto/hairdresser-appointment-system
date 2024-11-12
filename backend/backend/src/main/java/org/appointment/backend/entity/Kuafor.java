package org.appointment.backend.entity;

import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "kuaforler")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Kuafor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kuaforId;

    @Column(nullable = false)
    private String ad;

    @Column(nullable = false)
    private String soyad;

    @Enumerated(EnumType.STRING)
    @Column(length = 10 ,name="cinsiyet" )
    private Cinsiyet cinsiyet;

    @Column(nullable = false)
    private String telefon;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "kuafor")
    private List<Randevu> randevular;


    //Tablo oluşturuluyor
    @ManyToMany
    @JoinTable(
            name = "kuafor_hizmetler",
            joinColumns = @JoinColumn(name = "kuafor_id"),
            inverseJoinColumns = @JoinColumn(name = "hizmet_id")
    )
    private List<Hizmet> yapabilecegiHizmetler;


}