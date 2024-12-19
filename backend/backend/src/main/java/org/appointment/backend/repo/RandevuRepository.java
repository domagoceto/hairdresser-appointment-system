package org.appointment.backend.repo;

import org.appointment.backend.entity.Randevu;
import org.appointment.backend.entity.Kuafor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RandevuRepository extends JpaRepository<Randevu, Long> {
    List<Randevu> findAll(); // Tüm randevuları listeleme
    List<Randevu> findByKuafor_KuaforId(Long kuaforId);
    List<Randevu> findByKullanici_KullaniciId(Long kullaniciId);
    List<Randevu> findByKuafor_KuaforIdAndTarihAndSaat(Long kuaforId, LocalDate tarih, LocalTime saat);
    List<Randevu> findByKuafor_KuaforIdAndTarihAndSaatAndRandevuIdNot(
            Long kuaforId, LocalDate tarih, LocalTime saat, Long randevuId
    );

    List<Randevu> findByKuafor_KuaforIdAndTarih(Long kuaforId, LocalDate tarih);




}
