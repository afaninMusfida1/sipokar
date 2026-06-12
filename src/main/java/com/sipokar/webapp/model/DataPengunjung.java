package com.sipokar.webapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "data_pengunjung")
@Data
@NoArgsConstructor
public class DataPengunjung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate tanggal;

    @Column(name = "jumlah_pengunjung", nullable = false)
    private Integer jumlahPengunjung;
}