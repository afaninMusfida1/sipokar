package com.sipokar.webapp.controller.umkm;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.service.KeuanganService;
import com.sipokar.webapp.service.UmkmService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/umkm/dashboard")
@RequiredArgsConstructor
public class UmkmDashboardController {

    private final UmkmService umkmService;
    private final KeuanganService keuanganService;

    @GetMapping
    public Map<String, Object> dashboard(Authentication auth) {
        Umkm umkm = umkmService.getCurrentUmkm(auth)
                .orElseThrow(() -> new RuntimeException("UMKM tidak ditemukan"));
        Long umkmId = umkm.getId();

        BigDecimal pemasukan = keuanganService.totalPemasukan(umkmId);
        BigDecimal pengeluaran = keuanganService.totalPengeluaran(umkmId);
        BigDecimal saldo = keuanganService.saldo(umkmId);

        Map<String, Object> response = new HashMap<>();
        response.put("totalPemasukan", pemasukan);
        response.put("totalPengeluaran", pengeluaran);
        response.put("saldo", saldo);
        return response;
    }
}