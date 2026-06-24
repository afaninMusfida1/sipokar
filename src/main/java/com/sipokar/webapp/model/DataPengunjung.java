package com.sipokar.webapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "data_pengunjung")
@Data
@NoArgsConstructor
public class DataPengunjung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    @Column(name = "kota_asal", nullable = false)
    private String kotaAsal;

    @Column(name = "tanggal_kunjungan", nullable = false)
    private LocalDate tanggalKunjungan;

    @Column(name = "jumlah_orang", nullable = false)
    private Integer jumlahOrang = 1;

    @Column(name = "waktu_check_in")
    private LocalDateTime waktuCheckIn = LocalDateTime.now();
}