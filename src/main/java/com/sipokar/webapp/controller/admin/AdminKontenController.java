package com.sipokar.webapp.controller.admin;

import com.sipokar.webapp.model.WisataInfo;
import com.sipokar.webapp.repository.WisataInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/konten")
@RequiredArgsConstructor
public class AdminKontenController {

    private final WisataInfoRepository wisataInfoRepository;

    @GetMapping
    public String kontenForm(Model model) {
        WisataInfo info = wisataInfoRepository.findById(1L)
                .orElseGet(WisataInfo::new);
        model.addAttribute("info", info);
        return "admin/konten";
    }

    @PostMapping
    public String simpanKonten(@ModelAttribute WisataInfo info) {
        // Pastikan record konten utama selalu ID 1 (single-row content)
        WisataInfo existing = wisataInfoRepository.findById(1L).orElseGet(WisataInfo::new);

        existing.setDeskripsi(info.getDeskripsi());
        existing.setJamBuka(info.getJamBuka());
        existing.setJamTutup(info.getJamTutup());
        existing.setHargaTiket(info.getHargaTiket());
        existing.setInfoParkir(info.getInfoParkir());
        existing.setKontak(info.getKontak());
        existing.setFotoUrl(info.getFotoUrl());
        existing.setMapsUrl(info.getMapsUrl());

        wisataInfoRepository.save(existing);
        return "redirect:/admin/konten?success=true";
    }
}
