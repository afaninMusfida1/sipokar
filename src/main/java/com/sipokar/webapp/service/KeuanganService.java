package com.sipokar.webapp.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sipokar.webapp.model.Keuangan;
import com.sipokar.webapp.repository.KeuanganRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeuanganService {

    private final KeuanganRepository keuanganRepository;

    // === Overload untuk bulan berjalan ===
    public BigDecimal totalPemasukan(Long umkmId) {
        YearMonth now = YearMonth.now();
        return totalPemasukan(umkmId, now.getYear(), now.getMonthValue());
    }

    public BigDecimal totalPengeluaran(Long umkmId) {
        YearMonth now = YearMonth.now();
        return totalPengeluaran(umkmId, now.getYear(), now.getMonthValue());
    }

    public BigDecimal saldo(Long umkmId) {
        YearMonth now = YearMonth.now();
        return saldo(umkmId, now.getYear(), now.getMonthValue());
    }

    // === Method dengan parameter bulan ===
    public List<Keuangan> riwayat(Long umkmId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return keuanganRepository.findByUmkmIdAndTanggalBetween(umkmId, start, end);
    }

    public BigDecimal totalPemasukan(Long umkmId, int year, int month) {
        return riwayat(umkmId, year, month).stream()
                .filter(k -> k.getTipe() == Keuangan.Tipe.PEMASUKAN)
                .map(Keuangan::getNominal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalPengeluaran(Long umkmId, int year, int month) {
        return riwayat(umkmId, year, month).stream()
                .filter(k -> k.getTipe() == Keuangan.Tipe.PENGELUARAN)
                .map(Keuangan::getNominal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal saldo(Long umkmId, int year, int month) {
        return totalPemasukan(umkmId, year, month)
                .subtract(totalPengeluaran(umkmId, year, month));
    }

    // === Method untuk controller (find by date range) ===
    public List<Keuangan> findByUmkmAndTanggalBetween(Long umkmId, LocalDate start, LocalDate end) {
        return keuanganRepository.findByUmkmIdAndTanggalBetween(umkmId, start, end);
    }

    public Keuangan simpan(Keuangan keuangan) {
        return keuanganRepository.save(keuangan);
    }
}