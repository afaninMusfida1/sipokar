package com.sipokar.webapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    @Column(name = "email")
    private String email;

    // Variabel 'isi' yang tadinya hilang sudah dikembalikan ke sini
    @Column(nullable = false, columnDefinition = "TEXT")
    private String isi;

    @Column(name = "sudah_ditindaklanjuti")
    private boolean sudahDitindaklanjuti = false;

    @Column(name = "catatan_respons", columnDefinition = "TEXT")
    private String catatanRespons;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}