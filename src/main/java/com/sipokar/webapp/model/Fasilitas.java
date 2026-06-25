package com.sipokar.webapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fasilitas")
@Data
@NoArgsConstructor
public class Fasilitas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    @Column(name = "foto")
    private String foto; 
    
    // Tambahan untuk menyesuaikan HTML
    @Column(columnDefinition = "TEXT")
    private String deskripsi;
    
    @Column(name = "info_tambahan")
    private String infoTambahan;
}