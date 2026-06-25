package com.sipokar.webapp.controller.admin;

import com.sipokar.webapp.model.Fasilitas;
import com.sipokar.webapp.repository.FasilitasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin/fasilitas")
@RequiredArgsConstructor
public class AdminFasilitasController {

    private final FasilitasRepository fasilitasRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @GetMapping
    public String daftar(Model model) {
        model.addAttribute("daftarFasilitas", fasilitasRepository.findAll());
        model.addAttribute("fasilitas", new Fasilitas());
        return "admin/fasilitas";
    }

    @PostMapping
    public String tambah(@ModelAttribute Fasilitas fasilitas,
                          @RequestParam("file") MultipartFile file) throws IOException {

        fasilitas.setId(null);

        if (!file.isEmpty()) {
            String namaFile = simpanFile(file);
            fasilitas.setFoto(namaFile);
        }

        fasilitasRepository.save(fasilitas);
        return "redirect:/admin/fasilitas";
    }

    @PostMapping("/{id}/hapus")
    public String hapus(@PathVariable Long id) {
        Optional<Fasilitas> existing = fasilitasRepository.findById(id);
        existing.ifPresent(f -> {
            if (f.getFoto() != null) {
                try {
                    Files.deleteIfExists(Paths.get(uploadDir, f.getFoto()));
                } catch (IOException e) {
                    // log saja, jangan sampai gagalkan proses hapus data
                }
            }
        });
        fasilitasRepository.deleteById(id);
        return "redirect:/admin/fasilitas";
    }

    private String simpanFile(MultipartFile file) throws IOException {
        Path dir = Paths.get(uploadDir);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        String ekstensi = "";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            ekstensi = original.substring(original.lastIndexOf("."));
        }

        String namaFile = UUID.randomUUID() + ekstensi;
        Path target = dir.resolve(namaFile);
        file.transferTo(target);

        return namaFile;
    }
}