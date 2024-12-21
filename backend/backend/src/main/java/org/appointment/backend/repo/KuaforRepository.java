package org.appointment.backend.repo;

import org.appointment.backend.entity.Kuafor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KuaforRepository extends JpaRepository<Kuafor, Long> {
    Optional<Kuafor> findByEmail(String email);
    List<Kuafor> findByKullanici_KullaniciId(Long kullaniciId);
    @EntityGraph(attributePaths = "yapabilecegiHizmetler") // yapabilecegiHizmetler alanı için Eager Fetch
    Optional<Kuafor> findWithHizmetlerByKuaforId(Long kuaforId);
}

