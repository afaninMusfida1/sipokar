package com.sipokar.webapp.repository;

import com.sipokar.webapp.model.Fasilitas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FasilitasRepository extends JpaRepository<Fasilitas, Long> {
}