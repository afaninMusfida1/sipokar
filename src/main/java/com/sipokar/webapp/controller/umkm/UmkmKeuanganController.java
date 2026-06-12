package com.sipokar.webapp.controller.umkm;

import com.sipokar.webapp.model.Keuangan;
import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.repository.KeuanganRepository;
import com.sipokar.webapp.service.KeuanganService;
import com.sipokar.webapp.service.UmkmService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/umkm/keuangan")
@RequiredArgsConstructor
public class UmkmKeuanganController {

    private final UmkmService umkmService;
    private final KeuanganService keuanganService;
    private final KeuanganRepository keuanganRepository;

    @GetMapping
    public String rekap(Authentication auth, Model model) {
        Umkm umkm = umkmService.getCurrentUmkm(auth);

        if (umkm.getStatus() != Umkm.Status.VERIFIED) {
            return "redirect:/umkm/dashboard";
        }

        model.addAttribute("umkm", umkm);
        model.addAttribute("riwayat", keuanganService.riwayat(umkm.getId()));
        model.addAttribute("totalPemasukan", keuanganService.totalPemasukan(umkm.getId()));
        model.addAttribute("totalPengeluaran", keuanganService.totalPengeluaran(umkm.getId()));
        model.addAttribute("saldo", keuanganService.saldo(umkm.getId()));
        model.addAttribute("keuangan", new Keuangan());

        return "umkm/keuangan";
    }

    @PostMapping
    public String simpan(@ModelAttribute Keuangan keuangan, Authentication auth) {
        Umkm umkm = umkmService.getCurrentUmkm(auth);

        if (umkm.getStatus() != Umkm.Status.VERIFIED) {
            return "redirect:/umkm/dashboard";
        }

        keuangan.setId(null);
        keuangan.setUmkm(umkm);
        keuanganRepository.save(keuangan);

        return "redirect:/umkm/keuangan";
    }
}
