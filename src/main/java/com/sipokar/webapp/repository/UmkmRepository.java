package com.sipokar.webapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sipokar.webapp.model.Umkm;

@Repository
public interface UmkmRepository extends JpaRepository<Umkm, Long> {

    List<Umkm> findByStatus(Umkm.Status status);

    Optional<Umkm> findByUser_Id(Long userId);

    long countByStatus(Umkm.Status status);

    List<Umkm> findByStatusOrderByNamaUsahaAsc(Umkm.Status status);
}