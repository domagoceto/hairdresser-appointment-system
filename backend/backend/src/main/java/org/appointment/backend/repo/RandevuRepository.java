package org.appointment.backend.repo;

import org.appointment.backend.entity.Randevu;
import org.appointment.backend.entity.Kuafor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Randevu r WHERE r.kuafor.kuaforId = :kuaforId AND r.tarih = :tarih AND r.saat = :saat")
    boolean existsByKuaforIdAndTarihAndSaat(@Param("kuaforId") Long kuaforId,
                                            @Param("tarih") LocalDate tarih,
                                            @Param("saat") LocalTime saat);

}





