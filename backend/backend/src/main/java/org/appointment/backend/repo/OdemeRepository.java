package org.appointment.backend.repo;

import org.appointment.backend.entity.Odeme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OdemeRepository extends JpaRepository<Odeme, Long> {
    @Query("SELECT o FROM Odeme o " +
            "JOIN FETCH o.randevu r " +
            "JOIN FETCH r.hizmet h " +
            "JOIN FETCH o.kullanici k")
    List<Odeme> findAllWithDetails();
}
