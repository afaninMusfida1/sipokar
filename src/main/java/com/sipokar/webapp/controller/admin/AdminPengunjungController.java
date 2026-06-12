package com.sipokar.webapp.controller.admin;

import com.sipokar.webapp.model.DataPengunjung;
import com.sipokar.webapp.repository.DataPengunjungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/pengunjung")
@RequiredArgsConstructor
public class AdminPengunjungController {

    private final DataPengunjungRepository dataPengunjungRepository;

    @GetMapping
    public String form(Model model) {
        model.addAttribute("dataPengunjung", new DataPengunjung());
        model.addAttribute("riwayat", dataPengunjungRepository.findTop30ByOrderByTanggalDesc());
        return "admin/pengunjung";
    }

    @PostMapping
    public String simpan(@ModelAttribute DataPengunjung dataPengunjung) {
        // jika tanggal sudah ada, update; jika belum, buat baru
        dataPengunjungRepository.findByTanggal(dataPengunjung.getTanggal())
                .ifPresentOrElse(existing -> {
                    existing.setJumlahPengunjung(dataPengunjung.getJumlahPengunjung());
                    dataPengunjungRepository.save(existing);
                }, () -> {
                    dataPengunjung.setId(null);
                    dataPengunjungRepository.save(dataPengunjung);
                });
        return "redirect:/admin/pengunjung";
    }
}