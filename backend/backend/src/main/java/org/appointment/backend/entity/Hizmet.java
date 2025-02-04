package org.appointment.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "hizmetler")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Hizmet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long hizmetId;

    @Column(nullable = false)
    private String ad;

    @Column(length = 500)
    private String aciklama;

    @Column(nullable = false)
    private Integer sure;  // dakika cinsinden

    @Column(nullable = false)
    private Double fiyat;

    @OneToMany(mappedBy = "hizmet")
    private List<Randevu> randevular;

    @Column
    private String imageUrl;

    @ManyToMany(mappedBy = "yapabilecegiHizmetler", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Set<Kuafor> kuaforler = new HashSet<>();

}



