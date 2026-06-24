package com.sipokar.webapp.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sipokar.webapp.model.WisataInfo;
import com.sipokar.webapp.repository.WisataInfoRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/konten")
@RequiredArgsConstructor
public class AdminKontenController {

    private final WisataInfoRepository wisataInfoRepository;

    @GetMapping
    public String getAll(Model model) {
        // 1. Ambil semua data dari database
        List<WisataInfo> listInfo = wisataInfoRepository.findAll();
        
        // 2. Logika untuk mengisi Form
        WisataInfo infoForm;
        if (listInfo.isEmpty()) {
            infoForm = new WisataInfo(); // Jika database masih kosong, kasih form kosong
        } else {
            infoForm = listInfo.get(0);  // Jika sudah ada data, ambil data paling atas (index 0) untuk di-edit
        }

        // 3. Lempar ke Thymeleaf
        model.addAttribute("infoWisata", listInfo); // Opsional kalau kamu pakai untuk tabel
        model.addAttribute("info", infoForm);       // INI YANG PENTING: Untuk pre-fill value form HTML
        
        return "admin/konten"; 
    }

    @PostMapping
    public String create(@ModelAttribute("info") WisataInfo wisataInfo) { 
        // Menyimpan data. Jika objek punya ID (karena ditarik dari form), JPA otomatis melakukan UPDATE (Edit).
        wisataInfoRepository.save(wisataInfo);
        
        return "redirect:/admin/konten";
    }
}