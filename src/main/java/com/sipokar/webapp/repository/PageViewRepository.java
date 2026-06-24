package com.sipokar.webapp.repository;

import com.sipokar.webapp.model.PageView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PageViewRepository extends JpaRepository<PageView, Long> {
    Optional<PageView> findByTanggal(LocalDate tanggal);

    @org.springframework.data.jpa.repository.Query("SELECT COALESCE(SUM(p.jumlah), 0) FROM PageView p")
    Long totalKunjungan();
}