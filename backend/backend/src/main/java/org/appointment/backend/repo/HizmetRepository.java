package org.appointment.backend.repo;

import org.appointment.backend.entity.Hizmet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HizmetRepository extends JpaRepository<Hizmet, Long> {
}