package com.sipokar.webapp.repository;

import com.sipokar.webapp.model.DataPengunjung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DataPengunjungRepository extends JpaRepository<DataPengunjung, Long> {

    // Ambil 50 check-in terbaru
    List<DataPengunjung> findTop50ByOrderByWaktuCheckInDesc();

    // Ambil data berdasarkan tanggal kunjungan
    List<DataPengunjung> findByTanggalKunjunganOrderByWaktuCheckInDesc(LocalDate tanggal);

    // Hitung jumlah data berdasarkan tanggal
    long countByTanggalKunjungan(LocalDate tanggal);

    // Ambil 30 data terbaru berdasarkan tanggal kunjungan
    List<DataPengunjung> findTop30ByOrderByTanggalKunjunganDesc();

    // Total pengunjung per hari (untuk chart)
    @Query("""
                SELECT d.tanggalKunjungan, SUM(d.jumlahOrang)
                FROM DataPengunjung d
                GROUP BY d.tanggalKunjungan
                ORDER BY d.tanggalKunjungan DESC
            """)
    List<Object[]> findDailyVisitorTotals();

    // Total pengunjung bulan ini (untuk landing page)
    @Query("""
                SELECT COALESCE(SUM(d.jumlahOrang), 0)
                FROM DataPengunjung d
                WHERE MONTH(d.tanggalKunjungan) = MONTH(CURRENT_DATE)
                AND YEAR(d.tanggalKunjungan) = YEAR(CURRENT_DATE)
            """)
    Long countVisitorsThisMonth();
}