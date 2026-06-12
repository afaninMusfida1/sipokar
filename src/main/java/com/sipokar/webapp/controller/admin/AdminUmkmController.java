package com.sipokar.webapp.controller.admin;

import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.repository.UmkmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/umkm")
@RequiredArgsConstructor
public class AdminUmkmController {

    private final UmkmRepository umkmRepository;

    @GetMapping
    public String daftarUmkm(Model model) {
        model.addAttribute("daftarUmkm", umkmRepository.findAll());
        return "admin/umkm";
    }

    @GetMapping("/{id}")
    public String detailUmkm(@PathVariable Long id, Model model) {
        Umkm umkm = umkmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UMKM tidak ditemukan"));
        model.addAttribute("umkm", umkm);
        return "admin/umkm-detail";
    }

    @PostMapping("/{id}/verifikasi")
    public String verifikasi(@PathVariable Long id) {
        Umkm umkm = umkmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UMKM tidak ditemukan"));
        umkm.setStatus(Umkm.Status.VERIFIED);
        umkmRepository.save(umkm);
        return "redirect:/admin/umkm";
    }

    @PostMapping("/{id}/tolak")
    public String tolak(@PathVariable Long id) {
        Umkm umkm = umkmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UMKM tidak ditemukan"));
        umkm.setStatus(Umkm.Status.REJECTED);
        umkmRepository.save(umkm);
        return "redirect:/admin/umkm";
    }
}
