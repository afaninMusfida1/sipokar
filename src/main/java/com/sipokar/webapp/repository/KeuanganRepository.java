package com.sipokar.webapp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sipokar.webapp.model.Keuangan;
import com.sipokar.webapp.model.Keuangan.Kategori;
import com.sipokar.webapp.model.Keuangan.Tipe;

@Repository
public interface KeuanganRepository extends JpaRepository<Keuangan, Long> {
    // Metode existing
    List<Keuangan> findByUmkm_IdOrderByTanggalDesc(Long umkmId);
    List<Keuangan> findByUmkm_IdAndTipeOrderByTanggalDesc(Long umkmId, Tipe tipe);
    List<Keuangan> findByUmkm_IdAndKategoriOrderByTanggalDesc(Long umkmId, Kategori kategori);
    List<Keuangan> findByUmkm_IdAndTipeAndKategoriOrderByTanggalDesc(Long umkmId, Tipe tipe, Kategori kategori);
    List<Keuangan> findByUmkm_IdAndKeteranganContainingIgnoreCaseOrderByTanggalDesc(Long umkmId, String keyword);
    List<Keuangan> findByUmkmId(Long umkmId);

    List<Keuangan> findByUmkmIdAndTanggalBetween(Long umkmId, LocalDate start, LocalDate end);
}