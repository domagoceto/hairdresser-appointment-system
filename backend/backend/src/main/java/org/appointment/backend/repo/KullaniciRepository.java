package org.appointment.backend.repo;

import org.appointment.backend.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;


public interface KullaniciRepository extends JpaRepository<Kullanici,Long> {
}