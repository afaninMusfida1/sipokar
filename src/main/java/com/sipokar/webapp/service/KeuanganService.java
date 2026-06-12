package com.sipokar.webapp.service;

import com.sipokar.webapp.model.Keuangan;
import com.sipokar.webapp.repository.KeuanganRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeuanganService {

    private final KeuanganRepository keuanganRepository;

    public List<Keuangan> riwayat(Long umkmId) {
        return keuanganRepository.findByUmkm_IdOrderByTanggalDesc(umkmId);
    }

    public BigDecimal totalPemasukan(Long umkmId) {
        return riwayat(umkmId).stream()
                .filter(k -> k.getTipe() == Keuangan.Tipe.PEMASUKAN)
                .map(Keuangan::getNominal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalPengeluaran(Long umkmId) {
        return riwayat(umkmId).stream()
                .filter(k -> k.getTipe() == Keuangan.Tipe.PENGELUARAN)
                .map(Keuangan::getNominal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal saldo(Long umkmId) {
        return totalPemasukan(umkmId).subtract(totalPengeluaran(umkmId));
    }
}
