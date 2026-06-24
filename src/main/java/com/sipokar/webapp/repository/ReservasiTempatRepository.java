package com.sipokar.webapp.repository;

import com.sipokar.webapp.model.ReservasiTempat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservasiTempatRepository extends JpaRepository<ReservasiTempat, Long> {
}
