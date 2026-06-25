package com.sipokar.webapp.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

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