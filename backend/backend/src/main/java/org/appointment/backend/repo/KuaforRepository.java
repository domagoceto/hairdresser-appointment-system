package org.appointment.backend.repo;

import org.appointment.backend.entity.Kuafor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KuaforRepository extends JpaRepository<Kuafor, Long> {
}
