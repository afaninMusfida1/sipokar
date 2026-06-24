package com.sipokar.webapp.controller.umkm;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sipokar.webapp.model.Keuangan;
import com.sipokar.webapp.model.Retribusi;
import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.service.KeuanganService;
import com.sipokar.webapp.service.RetribusiService;
import com.sipokar.webapp.service.UmkmService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/umkm/keuangan")
@RequiredArgsConstructor
public class UmkmKeuanganController {

    private final KeuanganService keuanganService;
    private final RetribusiService retribusiService;
    private final UmkmService umkmService;

    @GetMapping
    public String halamanKeuangan(Authentication auth, Model model) {
        // 1. Ambil UMKM yang sedang login
        Umkm umkm = umkmService.getCurrentUmkm(auth)
                .orElseThrow(() -> new RuntimeException("UMKM tidak ditemukan"));
        
        Long umkmId = umkm.getId();

        // 2. AMBIL SEMUA DATA TANPA FILTER TANGGAL
        List<Keuangan> transaksi = keuanganService.findByUmkmId(umkmId);
        List<Retribusi> retribusiList = retribusiService.findByUmkmId(umkmId);

        // 3. Hitung total dari semua data
        BigDecimal totalPemasukan = transaksi.stream()
                .filter(k -> k.getTipe() == Keuangan.Tipe.PEMASUKAN)
                .map(Keuangan::getNominal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPengeluaran = transaksi.stream()
                .filter(k -> k.getTipe() == Keuangan.Tipe.PENGELUARAN)
                .map(Keuangan::getNominal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. Masukkan ke Model untuk dibaca HTML
        model.addAttribute("umkm", umkm);
        model.addAttribute("transaksi", transaksi);
        model.addAttribute("retribusiList", retribusiList);
        model.addAttribute("totalPemasukan", totalPemasukan);
        model.addAttribute("totalPengeluaran", totalPengeluaran);
        model.addAttribute("saldo", totalPemasukan.subtract(totalPengeluaran));
        
        // Tetap kirimkan parameter bulan saat ini untuk label di UI
        LocalDate today = LocalDate.now();
        model.addAttribute("selectedYear", today.getYear());
        model.addAttribute("selectedMonth", today.getMonthValue());

        return "umkm/keuangan";
    }

    @PostMapping("/transaksi")
    public String simpanTransaksi(
            Authentication auth,
            @RequestParam LocalDate tanggal,
            @RequestParam String keterangan,
            @RequestParam Keuangan.Tipe tipe,
            @RequestParam BigDecimal nominal) {

        Umkm umkm = umkmService.getCurrentUmkm(auth)
                .orElseThrow(() -> new RuntimeException("UMKM tidak ditemukan"));

        Keuangan transaksiBaru = new Keuangan();
        transaksiBaru.setUmkm(umkm);
        transaksiBaru.setTanggal(tanggal);
        transaksiBaru.setKeterangan(keterangan);
        transaksiBaru.setTipe(tipe);
        transaksiBaru.setNominal(nominal);

        keuanganService.save(transaksiBaru);

        // URL kembali bersih, tidak ada param ?year=...
        return "redirect:/umkm/keuangan";
    }
}