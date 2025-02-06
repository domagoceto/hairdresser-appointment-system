package org.appointment.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class RandevuRequest { //Randevu oluşturmak için gerekli verileri ileten Dto
    private Long kuaforId;
    private Long hizmetId;
    private LocalDate tarih;
    private LocalTime saat;
    private String notlar; //
}
