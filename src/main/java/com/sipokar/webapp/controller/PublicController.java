package com.sipokar.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sipokar.webapp.model.Feedback;
import com.sipokar.webapp.model.WisataInfo;
import com.sipokar.webapp.repository.FasilitasRepository; 
import com.sipokar.webapp.repository.FeedbackRepository;
import com.sipokar.webapp.repository.FotoGaleriRepository;
import com.sipokar.webapp.repository.WisataInfoRepository;
import com.sipokar.webapp.service.PageViewService;
import com.sipokar.webapp.service.ProdukService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PublicController {

    private final WisataInfoRepository wisataInfoRepository;
    private final FeedbackRepository feedbackRepository;
    private final FotoGaleriRepository fotoGaleriRepository;
    private final FasilitasRepository fasilitasRepository;
    private final PageViewService pageViewService;
    private final ProdukService produkService; 

    @GetMapping("/")
    public String landingPage(Model model) {
        WisataInfo info = wisataInfoRepository.findById(1L)
                .orElseGet(WisataInfo::new);

        model.addAttribute("info", info);
        model.addAttribute("galeri", fotoGaleriRepository.findAllByOrderByIdDesc());
        
        // INI YANG PALING PENTING - Namanya harus 'daftarFasilitas'
        model.addAttribute("daftarFasilitas", fasilitasRepository.findAll());

        model.addAttribute("produk", produkService.ambilSemuaProduk());

        pageViewService.recordVisit();

        // Print ke terminal buat ngecek data beneran ada atau nggak
        System.out.println("JUMLAH FASILITAS DITEMUKAN: " + fasilitasRepository.findAll().size());

        return "index";
    }

    @PostMapping("/feedback")
    public String submitFeedback(@RequestParam String nama,
                                 @RequestParam String pesan) {
        Feedback feedback = new Feedback();
        feedback.setNama(nama);
        feedback.setIsi(pesan);
        feedbackRepository.save(feedback);
        return "redirect:/?feedback=success";
    }

    @GetMapping("/produk-umkm")
    public String halamanSemuaProduk(Model model) {
        model.addAttribute("daftarProduk", produkService.ambilSemuaProduk());
        return "produk-umkm"; 
    }
}