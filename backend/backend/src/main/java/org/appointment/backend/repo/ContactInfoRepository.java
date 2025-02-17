package org.appointment.backend.repo;

import org.appointment.backend.entity.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactInfoRepository extends JpaRepository<ContactInfo, String> {
    List<ContactInfo> findAll();
}
