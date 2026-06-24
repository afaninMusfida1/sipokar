package com.sipokar.webapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wisata_info")
@Data
@NoArgsConstructor
public class WisataInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    @Column(name = "jam_buka")
    private String jamBuka;

    @Column(name = "jam_tutup")
    private String jamTutup;

    @Column(name = "harga_tiket")
    private String hargaTiket;

    @Column(name = "info_parkir", columnDefinition = "TEXT")
    private String infoParkir;

    private String kontak;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(name = "maps_url")
    private String mapsUrl;
}