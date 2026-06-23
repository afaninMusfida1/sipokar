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

    @Column(nullable = false)
    private LocalDate tanggal;

    @Column(nullable = false)
    private String keterangan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipe tipe; // PEMASUKAN / PENGELUARAN

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal nominal;

    // Kategori transaksi (sesuai pilihan di form)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Kategori kategori;

    // Relasi ke UMKM (sesuai pilihan di form)
    @ManyToOne
    @JoinColumn(name = "umkm_id", nullable = false)
    private Umkm umkm;

    // Opsional: untuk menyimpan bukti pembayaran (path file)
    private String bukti;

    // Opsional: status pembayaran (jika digunakan untuk kewajiban)
    private String status;

    public enum Tipe {
        PEMASUKAN, PENGELUARAN
    }

    public enum Kategori {
        TIKET,
        PERSEWAAN,
        DONASI,
        PERAWATAN,
        KEBERSIHAN,
        PAJAK,
        GAJI,
        RETRIBUSI
        // tambahkan sesuai kebutuhan
    }
}