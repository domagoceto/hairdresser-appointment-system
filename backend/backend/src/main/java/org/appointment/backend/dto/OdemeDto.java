package org.appointment.backend.dto;

import lombok.Data;
import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.entity.OdemeDurum;
import org.appointment.backend.entity.OdemeYontemi;
import org.appointment.backend.entity.Randevu;
import java.time.LocalDateTime;


@Data
public class OdemeDto {
    private Long odemeId;
    private Double tutar;
    private LocalDateTime odemeTarihi;
    private String durum;
    private Long kullaniciId;
    private Long randevuId;
    private String odemeYontemi;
    private String aciklama;
}
