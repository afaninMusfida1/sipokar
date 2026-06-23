package com.sipokar.webapp.controller.umkm;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sipokar.webapp.model.Keuangan;
import com.sipokar.webapp.model.Retribusi;
import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.service.KeuanganService;
import com.sipokar.webapp.service.RetribusiService;
import com.sipokar.webapp.service.UmkmService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/umkm/keuangan")
@RequiredArgsConstructor
public class UmkmKeuanganController {

    private final KeuanganService keuanganService;
    private final RetribusiService retribusiService;
    private final UmkmService umkmService;

    // Contoh endpoint: GET /api/umkm/keuangan/dashboard?umkmId=1&year=2026&month=6
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(
            @RequestParam Long umkmId,
            @RequestParam int year,
            @RequestParam int month) {

    if (!umkmService.findById(umkmId).isPresent()) {
    return ResponseEntity.badRequest().body("UMKM tidak ditemukan");
    }

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        // Ambil data transaksi dan retribusi
        List<Keuangan> transaksi = keuanganService.findByUmkmAndTanggalBetween(umkmId, start, end);
        List<Retribusi> retribusiList = retribusiService.findByUmkmAndTanggalBetween(umkmId, start, end);

        // Hitung total pemasukan & pengeluaran dengan BigDecimal
        BigDecimal totalPemasukan = transaksi.stream()
                .filter(k -> k.getTipe() == Keuangan.Tipe.PEMASUKAN)
                .map(Keuangan::getNominal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPengeluaran = transaksi.stream()
                .filter(k -> k.getTipe() == Keuangan.Tipe.PENGELUARAN)
                .map(Keuangan::getNominal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Buat response
        Map<String, Object> response = new HashMap<>();
        response.put("transactions", transaksi);
        response.put("retribusi", retribusiList);
        response.put("totalPemasukan", totalPemasukan);
        response.put("totalPengeluaran", totalPengeluaran);
        response.put("saldo", totalPemasukan.subtract(totalPengeluaran));

        return ResponseEntity.ok(response);
    }

    // Contoh endpoint untuk tambah retribusi
    @PostMapping("/retribusi")
    public ResponseEntity<?> tambahRetribusi(@RequestBody Map<String, Object> payload) {
        Long umkmId = Long.valueOf(payload.get("umkmId").toString());
        String jenis = payload.get("jenis").toString();
        BigDecimal nominal = new BigDecimal(payload.get("nominal").toString());
        LocalDate tanggal = LocalDate.parse(payload.get("tanggal").toString());
        String keterangan = payload.get("keterangan") != null ? payload.get("keterangan").toString() : null;
        String buktiPath = payload.get("buktiPath") != null ? payload.get("buktiPath").toString() : null;

        Umkm umkm = umkmService.findById(umkmId)
                .orElseThrow(() -> new RuntimeException("UMKM tidak ditemukan"));

        Retribusi retribusi = new Retribusi();
        retribusi.setUmkm(umkm);
        retribusi.setJenis(jenis);
        retribusi.setNominal(nominal);
        retribusi.setTanggal(tanggal);
        retribusi.setKeterangan(keterangan);
        retribusi.setBuktiPath(buktiPath);
        retribusi.setStatus(Retribusi.StatusRetribusi.MENUNGGU_VERIFIKASI); // pakai enum

        Retribusi saved = retribusiService.save(retribusi);
        return ResponseEntity.ok(saved);
    }
}