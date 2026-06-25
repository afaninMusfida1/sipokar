package com.sipokar.webapp.repository;

import com.sipokar.webapp.model.Reservasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservasiRepository extends JpaRepository<Reservasi, Long> {
    List<Reservasi> findByStatusAndTanggalGreaterThanEqual(Reservasi.Status status, LocalDate tanggal);
    List<Reservasi> findByTanggalGreaterThanEqualOrderByTanggalAsc(LocalDate tanggal);
    List<Reservasi> findByTempat_IdOrderByTanggalDesc(Long tempatId);
    List<Reservasi> findAllByOrderByCreatedAtDesc();
}
