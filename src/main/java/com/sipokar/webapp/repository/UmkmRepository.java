package com.sipokar.webapp.repository;

import com.sipokar.webapp.model.Umkm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UmkmRepository extends JpaRepository<Umkm, Long> {
    List<Umkm> findByStatus(Umkm.Status status);
    Optional<Umkm> findByUser_Id(Long userId);
    long countByStatus(Umkm.Status status);
}
