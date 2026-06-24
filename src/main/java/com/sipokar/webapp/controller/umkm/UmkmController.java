package com.sipokar.webapp.controller.umkm; // Sesuaikan dengan nama package kamu

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.util.StringUtils;

import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.repository.UmkmRepository;
import com.sipokar.webapp.service.UmkmService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/umkm") // <--- PERHATIKAN INI, HARUS /umkm
@RequiredArgsConstructor
public class UmkmController {

    private final UmkmService umkmService;
    private final UmkmRepository umkmRepository;

    // ... (Pastikan method dashboard dan daftar_umkm kamu yang lama tetap ada di sini) ...

    // ==============================================================
    // 1. Method untuk MENAMPILKAN Form Edit Revisi
    // ==============================================================
    @GetMapping("/edit-daftar")
    public String editDaftarForm(Authentication auth, Model model) {
        // Ambil data UMKM yang lagi login
        Umkm umkm = umkmService.getCurrentUmkm(auth)
                .orElseThrow(() -> new RuntimeException("UMKM tidak ditemukan"));
        
        model.addAttribute("umkm", umkm);
        return "umkm/edit-daftar"; // Merender file src/main/resources/templates/umkm/edit-daftar.html
    }

    // ==============================================================
    // 2. Method untuk MENYIMPAN Data Revisi
    // ==============================================================
    @PostMapping("/edit-daftar")
    public String submitEditDaftar(
            Authentication auth,
            @ModelAttribute Umkm umkmUpdate,
            @RequestParam(value = "fileBerkas", required = false) MultipartFile fileBerkas) {
        
        // 1. Ambil data lama dari database
        Umkm umkmExisting = umkmService.getCurrentUmkm(auth)
                .orElseThrow(() -> new RuntimeException("UMKM tidak ditemukan"));

        // 2. Update teks/data
        umkmExisting.setNamaUsaha(umkmUpdate.getNamaUsaha());
        umkmExisting.setJenisUsaha(umkmUpdate.getJenisUsaha());
        umkmExisting.setPemilik(umkmUpdate.getPemilik());
        umkmExisting.setTelepon(umkmUpdate.getTelepon());
        umkmExisting.setAlamat(umkmUpdate.getAlamat());

        // 3. PROSES UPLOAD FILE ASLI
        if (fileBerkas != null && !fileBerkas.isEmpty()) {
            try {
                // a. Bersihkan nama file aslinya (misal: "Proposal Aku.pdf")
                String fileName = StringUtils.cleanPath(fileBerkas.getOriginalFilename());
                
                // b. Tambahkan angka unik (timestamp) biar nama file gak bentrok kalau ada yang sama
                String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
                
                // c. Tentukan folder penyimpanan (Kita simpan di folder static/uploads)
                String uploadDir = "uploads/";
                Path uploadPath = Paths.get(uploadDir);
                
                // d. Buat folder 'uploads' secara otomatis kalau belum ada
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                // e. Copy/Simpan file fisik ke folder tersebut
                Path filePath = uploadPath.resolve(uniqueFileName);
                Files.copy(fileBerkas.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                
                // f. Simpan path-nya ke database (Ini yang akan dibaca oleh HTML)
                umkmExisting.setBerkasPendukungPath("/uploads/" + uniqueFileName);
                
            } catch (IOException e) {
                e.printStackTrace();
                // Kalau gagal upload, kembalikan ke form
                return "redirect:/umkm/edit-daftar?error=gagal_upload";
            }
        }

        // 4. Ubah statusnya kembali jadi PENDING biar admin bisa nge-review ulang
        umkmExisting.setStatus(Umkm.Status.PENDING);
        
        // 5. Simpan perubahan ke database
        umkmRepository.save(umkmExisting);

        return "redirect:/umkm/dashboard";
    }
}