package com.sipokar.webapp.repository;

import com.sipokar.webapp.model.Keuangan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeuanganRepository extends JpaRepository<Keuangan, Long> {
    List<Keuangan> findByUmkm_IdOrderByTanggalDesc(Long umkmId);
}
