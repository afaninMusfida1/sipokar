package com.sipokar.webapp.repository;

import com.sipokar.webapp.model.PageView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PageViewRepository extends JpaRepository<PageView, Long> {

    Optional<PageView> findByTanggal(LocalDate tanggal);

    @Query("SELECT COALESCE(SUM(p.jumlah), 0) FROM PageView p")
    Long totalKunjungan();

    @Query("""
                SELECT COALESCE(SUM(p.jumlah), 0)
                FROM PageView p
                WHERE MONTH(p.tanggal) = :month
                AND YEAR(p.tanggal) = YEAR(CURRENT_DATE)
            """)
    Long countByMonth(int month);
}