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

    @Column(nullable = false)
    private String ad;

    @Column(nullable = false)
    private String soyad;

    @Column(nullable = false)
    private String sifre;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, name = "cinsiyet")
    private Cinsiyet cinsiyet;

    @Column(nullable = false)
    private String telefon;

    @Column(nullable = false,unique = true)
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
}

