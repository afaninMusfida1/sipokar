package com.sipokar.webapp.controller.admin;

import com.sipokar.webapp.model.FotoGaleri;
import com.sipokar.webapp.repository.FotoGaleriRepository;
import com.sipokar.webapp.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/galeri")
@RequiredArgsConstructor
public class AdminGaleriController {

    private final FotoGaleriRepository fotoGaleriRepository;
    private final CloudinaryService cloudinaryService;

    @GetMapping
    public String daftar(Model model) {
        // Mengurutkan dari yang terbaru (ID terbesar)
        model.addAttribute("daftarFoto", fotoGaleriRepository.findAllByOrderByIdDesc());
        model.addAttribute("foto", new FotoGaleri());
        return "admin/galeri";
    }

    @PostMapping
    public String tambah(
            @ModelAttribute FotoGaleri foto,
            @RequestParam("gambar") MultipartFile gambar) {

        String imageUrl = cloudinaryService.uploadFile(gambar);

        foto.setUrl(imageUrl);

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