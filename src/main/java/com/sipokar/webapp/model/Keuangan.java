package com.sipokar.webapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "keuangan")
@Data
@NoArgsConstructor
public class Keuangan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate tanggal;

    @Column(nullable = false)
    private String keterangan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipe tipe;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal nominal;

    @ManyToOne
    @JoinColumn(name = "umkm_id", nullable = false)
    private Umkm umkm;

    public enum Tipe {
        PEMASUKAN, PENGELUARAN
    }
}