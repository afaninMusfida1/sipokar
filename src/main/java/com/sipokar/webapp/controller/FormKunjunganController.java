package com.sipokar.webapp.controller;

import com.sipokar.webapp.model.DataPengunjung;
import com.sipokar.webapp.service.PengunjungService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class FormKunjunganController {

    private final PengunjungService pengunjungService;

    @GetMapping({"/form-kunjungan"})
    public String form(Model model) {
        DataPengunjung dataPengunjung = new DataPengunjung();
        dataPengunjung.setTanggalKunjungan(LocalDate.now());
        dataPengunjung.setJumlahOrang(1);
        model.addAttribute("pengunjung", dataPengunjung);
        return "form-kunjungan";
    }

    @PostMapping({"/form-kunjungan"})
    public String simpan(@ModelAttribute("pengunjung") DataPengunjung dataPengunjung) {
        pengunjungService.simpan(dataPengunjung);
        return "redirect:/form-kunjungan?success=true";
    }
}
