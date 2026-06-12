package com.sipokar.webapp.repository;

import com.sipokar.webapp.model.FotoGaleri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FotoGaleriRepository extends JpaRepository<FotoGaleri, Long> {
    List<FotoGaleri> findAllByOrderByUrutanAsc();
}