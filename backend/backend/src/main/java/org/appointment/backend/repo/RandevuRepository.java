package org.appointment.backend.repo;


import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.entity.Randevu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RandevuRepository extends JpaRepository<Randevu,Long> {
    List<Randevu> findByKullanici(Kullanici kullanici);
}
