package com.sipokar.webapp.controller.umkm;

import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.service.KeuanganService;
import com.sipokar.webapp.service.UmkmService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/umkm/dashboard")
@RequiredArgsConstructor
public class UmkmDashboardController {

    private final UmkmService umkmService;
    private final KeuanganService keuanganService;

    @GetMapping
    public String dashboard(Authentication auth, Model model) {
        Umkm umkm = umkmService.getCurrentUmkm(auth);
        model.addAttribute("umkm", umkm);

        if (umkm.getStatus() == Umkm.Status.VERIFIED) {
            model.addAttribute("totalPemasukan", keuanganService.totalPemasukan(umkm.getId()));
            model.addAttribute("totalPengeluaran", keuanganService.totalPengeluaran(umkm.getId()));
            model.addAttribute("saldo", keuanganService.saldo(umkm.getId()));
        }

        return "umkm/dashboard";
    }
}
