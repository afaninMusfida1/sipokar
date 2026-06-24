package com.sipokar.webapp.controller.umkm;

import java.math.BigDecimal;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Tambahkan import ini
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.service.KeuanganService;
import com.sipokar.webapp.service.UmkmService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/umkm/dashboard")
@RequiredArgsConstructor
public class UmkmDashboardController {

    private final UmkmService umkmService;
    private final KeuanganService keuanganService;

    @GetMapping
    public String dashboard(Authentication auth, Model model) {
        Umkm umkm = umkmService.getCurrentUmkm(auth)
                .orElseThrow(() -> new RuntimeException("UMKM tidak ditemukan"));
        
        Long umkmId = umkm.getId();

        BigDecimal pemasukan = keuanganService.totalPemasukan(umkmId);
        BigDecimal pengeluaran = keuanganService.totalPengeluaran(umkmId);
        BigDecimal saldo = keuanganService.saldo(umkmId);

        // 1. Masukkan semua data ke dalam Model
        model.addAttribute("umkm", umkm); // <--- INI KUNCI UTAMANYA
        model.addAttribute("totalPemasukan", pemasukan);
        model.addAttribute("totalPengeluaran", pengeluaran);
        model.addAttribute("saldo", saldo);

        // 2. Return nama file template (sesuai letak: templates/umkm/dashboard.html)
        return "umkm/dashboard";
    }
}