package org.appointment.backend.entity;

import lombok.*;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "kuaforler")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Sadece belirttiğiniz alanları kullanır
public class Kuafor {

    @EqualsAndHashCode.Include // hashCode ve equals için kullanılacak alan
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kuaforId;

    @Column(length = 100, name = "ad")
    private String ad;

    @Column(length = 100, name = "soyad")
    private String soyad;

    @Column(length = 100, name = "sifre")
    private String sifre;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, name = "cinsiyet")
    private Cinsiyet cinsiyet;

    @Column(length = 15, name = "telefon")
    private String telefon;

    @Column(length = 100, name = "email", unique = true)
    private String email;

    @OneToMany(mappedBy = "kuafor")
    private List<Randevu> randevular;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "kuafor_hizmetler",
            joinColumns = @JoinColumn(name = "kuafor_id"),
            inverseJoinColumns = @JoinColumn(name = "hizmet_id")
    )
    private Set<Hizmet> yapabilecegiHizmetler = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kullanici_id", referencedColumnName = "kullaniciId")
    private Kullanici kullanici;

    private String kuaforKey;




}

