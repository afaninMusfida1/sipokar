package com.sipokar.webapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "produk")
public class Produk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nama;
    
    // --- PERBAIKAN: Menambahkan field deskripsi yang dicari Thymeleaf ---
    private String deskripsi; 
    
    private Long harga;
    private Integer diskon;
    private String gambar; // Isinya path file foto, misal: "/images/produk/kripik.jpg"

    // Relasi: Satu UMKM bisa punya banyak produk
    @ManyToOne
    @JoinColumn(name = "umkm_id", nullable = false)
    private Umkm umkm;

    // --- GETTER DAN SETTER ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    // --- PERBAIKAN: Menambahkan Getter dan Setter untuk deskripsi ---
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    // -----------------------------------------------------------------

    public Long getHarga() { return harga; }
    public void setHarga(Long harga) { this.harga = harga; }
    public Integer getDiskon() { return diskon; }
    public void setDiskon(Integer diskon) { this.diskon = diskon; }
    public String getGambar() { return gambar; }
    public void setGambar(String gambar) { this.gambar = gambar; }
    public Umkm getUmkm() { return umkm; }
    public void setUmkm(Umkm umkm) { this.umkm = umkm; }
    
}