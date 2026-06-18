package com.sipokar.webapp.repository;

import com.sipokar.webapp.model.DataPengunjung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DataPengunjungRepository extends JpaRepository<DataPengunjung, Long> {
    List<DataPengunjung> findTop50ByOrderByWaktuCheckInDesc();
    List<DataPengunjung> findByTanggalKunjunganOrderByWaktuCheckInDesc(LocalDate tanggal);
    long countByTanggalKunjungan(LocalDate tanggal);
    List<DataPengunjung> findTop30ByOrderByTanggalKunjunganDesc();

    @Query("SELECT d.tanggalKunjungan, SUM(d.jumlahOrang) FROM DataPengunjung d GROUP BY d.tanggalKunjungan ORDER BY d.tanggalKunjungan DESC")
    List<Object[]> findDailyVisitorTotals();
}