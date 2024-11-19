package org.appointment.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "hizmetler")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hizmet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToMany(mappedBy = "yapabilecegiHizmetler" , cascade = CascadeType.PERSIST)
    private List<Kuafor> kuaforler;


}

