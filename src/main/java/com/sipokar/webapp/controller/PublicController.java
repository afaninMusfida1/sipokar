package com.sipokar.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sipokar.webapp.model.Feedback;
import com.sipokar.webapp.model.WisataInfo;
import com.sipokar.webapp.repository.DataPengunjungRepository;
import com.sipokar.webapp.repository.FasilitasRepository;
import com.sipokar.webapp.repository.FeedbackRepository;
import com.sipokar.webapp.repository.FotoGaleriRepository;
import com.sipokar.webapp.repository.WisataInfoRepository;
import com.sipokar.webapp.repository.UmkmRepository;
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

    // TAMBAHAN BARU
    private final DataPengunjungRepository dataPengunjungRepository;
    private final UmkmRepository umkmRepository;

    @GetMapping("/")
    public String landingPage(Model model) {
        WisataInfo info = wisataInfoRepository.findById(1L)
                .orElseGet(WisataInfo::new);

        // record dulu
        pageViewService.recordVisit();

        // baru ambil total (sama persis seperti admin dashboard)
        long totalPengunjungBulanan = pageViewService.totalKunjungan();
        long totalUmkm = umkmRepository.count();

        model.addAttribute("info", info);
        model.addAttribute("galeri", fotoGaleriRepository.findAllByOrderByIdDesc());
        model.addAttribute("daftarFasilitas", fasilitasRepository.findAll());
        model.addAttribute("produk", produkService.ambilSemuaProduk());

        model.addAttribute("totalPengunjungBulanan", totalPengunjungBulanan);
        model.addAttribute("totalUmkm", totalUmkm);

        System.out.println("TOTAL PENGUNJUNG LANDING: " + totalPengunjungBulanan);
        System.out.println("TOTAL UMKM LANDING: " + totalUmkm);

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