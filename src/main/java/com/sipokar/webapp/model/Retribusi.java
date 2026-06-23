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
@Table(name = "retribusi")
@Data
@NoArgsConstructor
public class Retribusi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "umkm_id", nullable = false)
    @JsonIgnore 
    private Umkm umkm;

    @Column(nullable = false)
    private String jenis;               // "Pajak", "Iuran Sampah", "Lainnya"

    @Column(nullable = false)
    private BigDecimal nominal;

    @Column(nullable = false)
    private LocalDate tanggal;

    private String keterangan;

    @Column(name = "bukti_path")
    private String buktiPath;           

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusRetribusi status = StatusRetribusi.BELUM_BAYAR;

    public enum StatusRetribusi {
        BELUM_BAYAR,
        MENUNGGU_VERIFIKASI,
        LUNAS
    }
}