package com.sipokar.webapp.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "keuangan")
@Data
@NoArgsConstructor
public class Keuangan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "umkm_id", nullable = false)
    @JsonIgnore
    private Umkm umkm;

    @Column(nullable = false)
    private LocalDate tanggal;

    private String keterangan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipe tipe;

    @Enumerated(EnumType.STRING)
    private Kategori kategori;  // opsional, bisa null

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal nominal;

    public enum Tipe {
        PEMASUKAN, PENGELUARAN
    }

    public enum Kategori {
        SEWA, OPERASIONAL, RETRIBUSI, LAINNYA
    }
}