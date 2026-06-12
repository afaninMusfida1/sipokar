package com.sipokar.webapp.repository;

import com.sipokar.webapp.model.DataPengunjung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DataPengunjungRepository extends JpaRepository<DataPengunjung, Long> {
    List<DataPengunjung> findTop30ByOrderByTanggalDesc();
    Optional<DataPengunjung> findByTanggal(LocalDate tanggal);
}