package org.appointment.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class RandevuRequest {
    private Long kuaforId;
    private Long hizmetId;
    private LocalDate tarih;
    private LocalTime saat;
    private String notlar; // Kullanıcının isteğe bağlı notu
}
