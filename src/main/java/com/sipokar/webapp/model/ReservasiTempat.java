package com.sipokar.webapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tempat_reservasi")
@Data
@NoArgsConstructor
public class ReservasiTempat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    private Integer kapasitas;

    @Column(name = "foto_url")
    private String fotoUrl;
}