package org.appointment.backend.repo;


import org.appointment.backend.entity.Randevu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandevuRepository extends JpaRepository<Randevu,Long> {
}
