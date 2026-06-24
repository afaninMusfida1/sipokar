package com.sipokar.webapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "page_view")
@Data
@NoArgsConstructor
public class PageView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate tanggal;

    @Column(name = "jumlah", nullable = false)
    private Long jumlah = 0L;
}