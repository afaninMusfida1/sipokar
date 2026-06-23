package com.sipokar.webapp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sipokar.webapp.model.Retribusi;

@Repository
public interface RetribusiRepository extends JpaRepository<Retribusi, Long> {
    List<Retribusi> findByUmkmIdAndTanggalBetween(Long umkmId, LocalDate start, LocalDate end);
}