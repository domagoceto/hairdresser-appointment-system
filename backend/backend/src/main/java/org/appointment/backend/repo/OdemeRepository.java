package org.appointment.backend.repo;

import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.entity.Odeme;
import org.appointment.backend.entity.Randevu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OdemeRepository extends JpaRepository<Odeme, Long> {
    List<Odeme> findByKullanici(Kullanici kullanici);

    List<Odeme> findByRandevu(Randevu randevu);

}
