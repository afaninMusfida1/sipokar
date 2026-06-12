package com.sipokar.webapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "umkm")
@Data
@NoArgsConstructor
public class Umkm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_usaha", nullable = false)
    private String namaUsaha;

    @Column(name = "jenis_usaha")
    private String jenisUsaha;

    @Column(nullable = false)
    private String pemilik;

    private String telepon;
    private String alamat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public enum Status {
        PENDING, VERIFIED, REJECTED
    }
}