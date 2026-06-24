package com.sipokar.webapp.controller.admin;

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
        // Tarik spesifik ID 1. Jika belum ada di database, buat objek baru dan paksa ID-nya jadi 1.
        WisataInfo infoForm = wisataInfoRepository.findById(1L).orElseGet(() -> {
            WisataInfo newInfo = new WisataInfo();
            newInfo.setId(1L); 
            return newInfo;
        });

        model.addAttribute("info", infoForm);       
        return "admin/konten"; 
    }

    @PostMapping
    public String create(@ModelAttribute("info") WisataInfo wisataInfo) { 
        // Karena form HTML sekarang membawa <input type="hidden" name="id" value="1">
        // JPA otomatis akan mendeteksi ID tersebut dan melakukan UPDATE, bukan CREATE.
        wisataInfoRepository.save(wisataInfo);
        
        // Tambahkan parameter success agar alert hijau di HTML kamu muncul
        return "redirect:/admin/konten?success=true"; 
    }
}