package com.sipokar.webapp.controller.umkm;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.model.User;
import com.sipokar.webapp.repository.UmkmRepository;
import com.sipokar.webapp.service.CloudinaryService;
import com.sipokar.webapp.service.UmkmService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/umkm")
@RequiredArgsConstructor
public class UmkmRegisterController {

    private final UmkmRepository umkmRepository;
    private final UmkmService umkmService;
    private final CloudinaryService cloudinaryService;

    @GetMapping("/daftar")
    public String form(Authentication auth, Model model) {
        User user = umkmService.getCurrentUser(auth)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        if (umkmRepository.findByUser_Id(user.getId()).isPresent()) {
            return "redirect:/umkm/dashboard";
        }

        model.addAttribute("umkm", new Umkm());
        return "umkm/daftar";
    }

    @PostMapping("/daftar")
    public String submit(
            @ModelAttribute Umkm umkm,
            @RequestParam("fileBerkas") MultipartFile fileBerkas,
            Authentication auth,
            Model model) {

        User user = umkmService.getCurrentUser(auth)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        if (fileBerkas.isEmpty()) {
            model.addAttribute("error", "File proposal wajib diupload");
            model.addAttribute("umkm", umkm);
            return "umkm/daftar";
        }

        long maxSize = 5 * 1024 * 1024;

        if (fileBerkas.getSize() >= maxSize) {
            model.addAttribute("error", "Ukuran file maksimal 5MB");
            model.addAttribute("umkm", umkm);
            return "umkm/daftar";
        }

        if (!"application/pdf".equals(fileBerkas.getContentType())) {
            model.addAttribute("error", "File harus berformat PDF");
            model.addAttribute("umkm", umkm);
            return "umkm/daftar";
        }

        String fileUrl = cloudinaryService.uploadFile(fileBerkas, "sipokar-proposal");

        if (fileUrl == null) {
            model.addAttribute("error", "Upload gagal. Periksa ukuran atau format file.");
            model.addAttribute("umkm", umkm);
            return "umkm/daftar";
        }

        umkm.setId(null);
        umkm.setStatus(Umkm.Status.PENDING);
        umkm.setBerkasPendukungPath(fileUrl);
        umkm.setUser(user);

        umkmRepository.save(umkm);

        return "redirect:/umkm/dashboard";
    }
}