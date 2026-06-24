package com.sipokar.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sipokar.webapp.model.Produk;
import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.repository.ProdukRepository;

@Service
public class ProdukService {

    @Autowired
    private ProdukRepository produkRepository;

    // Ambil semua produk berdasarkan UMKM yang sedang login
    public List<Produk> ambilProdukPerUmkm(Umkm umkm) {
        return produkRepository.findByUmkm(umkm);
    }

    // Ambil semua produk untuk landing page utama (Sipokar)
    public List<Produk> ambilSemuaProduk() {
        return produkRepository.findAll();
    }

    public void simpanProduk(Produk produk) {
        produkRepository.save(produk);
    }

    public void hapusProduk(Long id) {
        produkRepository.deleteById(id);
    }

    public Produk ambilProdukBerdasarkanId(Long id) {
        return produkRepository.findById(id).orElse(null);
    }
}