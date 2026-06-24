package com.sipokar.webapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sipokar.webapp.model.Produk;
import com.sipokar.webapp.model.Umkm;

@Repository
public interface ProdukRepository extends JpaRepository<Produk, Long> {
    List<Produk> findByUmkm(Umkm umkm);
}