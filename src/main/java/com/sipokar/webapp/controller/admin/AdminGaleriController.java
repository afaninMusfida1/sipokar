package com.sipokar.webapp.controller.admin;

import com.sipokar.webapp.model.FotoGaleri;
import com.sipokar.webapp.repository.FotoGaleriRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/galeri")
@RequiredArgsConstructor
public class AdminGaleriController {

    private final FotoGaleriRepository fotoGaleriRepository;

    @GetMapping
    public String daftar(Model model) {
        model.addAttribute("daftarFoto", fotoGaleriRepository.findAllByOrderByUrutanAsc());
        model.addAttribute("foto", new FotoGaleri());
        return "admin/galeri";
    }

    @PostMapping
    public String tambah(@ModelAttribute FotoGaleri foto) {
        foto.setId(null);
        fotoGaleriRepository.save(foto);
        return "redirect:/admin/galeri";
    }

    @PostMapping("/{id}/hapus")
    public String hapus(@PathVariable Long id) {
        fotoGaleriRepository.deleteById(id);
        return "redirect:/admin/galeri";
    }
}