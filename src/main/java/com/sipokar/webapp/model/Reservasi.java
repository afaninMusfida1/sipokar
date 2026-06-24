package com.sipokar.webapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservasi")
@Data
@NoArgsConstructor
public class Reservasi {

    public enum Status {
        BOOKED,
        CANCELLED,
        PENDING
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_pemesan", nullable = false)
    private String namaPemesan;

    @Column(nullable = false)
    private String kontak;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tempat_id", nullable = false)
    private ReservasiTempat tempat;

    @Column(name = "tanggal_reservasi", nullable = false)
    private LocalDate tanggal;

  /** Kolom legacy di DB; disinkronkan dengan tanggal reservasi. */
    @Column(name = "tanggal", nullable = false)
    private LocalDate tanggalLegacy;

    @Column(name = "jumlah_orang")
    private Integer jumlahOrang;

    @Enumerated(EnumType.STRING)
    private Status status = Status.BOOKED;

    @Column(columnDefinition = "TEXT")
    private String catatan;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    @PreUpdate
    protected void onSave() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (tanggal != null) {
            tanggalLegacy = tanggal;
        } else if (tanggalLegacy != null) {
            tanggal = tanggalLegacy;
        }
    }
}
