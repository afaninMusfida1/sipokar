package com.sipokar.webapp.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sipokar.webapp.model.Keuangan;
import com.sipokar.webapp.repository.KeuanganRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeuanganService {

    private final KeuanganRepository keuanganRepository;

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

    public List<Keuangan> riwayat(Long umkmId, int year, int month) {
        List<Keuangan> all = keuanganRepository.findByUmkm_Id(umkmId);

        return all.stream()
                .filter(k -> {
                    String ym = k.getTanggal().format(
                            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));

                    String selected = String.format("%d-%02d", year, month);

                    return ym.equals(selected);
                })
                .toList();
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

    public List<Keuangan> findByUmkmAndTanggalBetween(
            Long umkmId,
            LocalDate start,
            LocalDate end) {

        return keuanganRepository.findByUmkm_IdAndTanggalBetween(
                umkmId,
                start,
                end);
    }

    public Keuangan save(Keuangan keuangan) {
        return keuanganRepository.save(keuangan);
    }

    public List<Keuangan> findByUmkmId(Long umkmId) {
        return keuanganRepository.findByUmkm_Id(umkmId);
    }

    public Optional<Keuangan> findById(Long id) {
        return keuanganRepository.findById(id);
    }
}