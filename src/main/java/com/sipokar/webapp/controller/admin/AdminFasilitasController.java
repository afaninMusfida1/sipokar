package com.sipokar.webapp.controller.admin;

import com.sipokar.webapp.model.Fasilitas;
import com.sipokar.webapp.repository.FasilitasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/fasilitas")
@RequiredArgsConstructor
public class AdminFasilitasController {

    private final FasilitasRepository fasilitasRepository;

    @GetMapping
    public String daftar(Model model) {
        model.addAttribute("daftarFasilitas", fasilitasRepository.findAll());
        model.addAttribute("fasilitas", new Fasilitas());
        return "admin/fasilitas";
    }

    @PostMapping
    public String tambah(@ModelAttribute Fasilitas fasilitas) {
        fasilitas.setId(null);
        fasilitasRepository.save(fasilitas);
        return "redirect:/admin/fasilitas";
    }

    @PostMapping("/{id}/hapus")
    public String hapus(@PathVariable Long id) {
        fasilitasRepository.deleteById(id);
        return "redirect:/admin/fasilitas";
    }
}