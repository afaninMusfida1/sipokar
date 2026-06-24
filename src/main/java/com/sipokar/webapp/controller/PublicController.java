package com.sipokar.webapp.controller;

<<<<<<< HEAD
=======
import com.sipokar.webapp.model.Feedback;
import com.sipokar.webapp.model.WisataInfo;
import com.sipokar.webapp.repository.FasilitasRepository;
import com.sipokar.webapp.repository.FeedbackRepository;
import com.sipokar.webapp.repository.FotoGaleriRepository;
import com.sipokar.webapp.repository.WisataInfoRepository;
import com.sipokar.webapp.service.PageViewService;
import lombok.RequiredArgsConstructor;
>>>>>>> f58bcb704ca04e133dbe4515d85768387958ed38
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

<<<<<<< HEAD
import com.sipokar.webapp.model.Feedback;
import com.sipokar.webapp.model.WisataInfo;
import com.sipokar.webapp.repository.FasilitasRepository; // 1. IMPORT BARU UNTUK PRODUK SERVICE
import com.sipokar.webapp.repository.FeedbackRepository;
import com.sipokar.webapp.repository.FotoGaleriRepository;
import com.sipokar.webapp.repository.WisataInfoRepository;
import com.sipokar.webapp.service.PageViewService;
import com.sipokar.webapp.service.ProdukService;

import lombok.RequiredArgsConstructor;

=======
>>>>>>> f58bcb704ca04e133dbe4515d85768387958ed38
@Controller
@RequiredArgsConstructor
public class PublicController {

    private final WisataInfoRepository wisataInfoRepository;
    private final FeedbackRepository feedbackRepository;
    private final FotoGaleriRepository fotoGaleriRepository;
    private final FasilitasRepository fasilitasRepository;
    private final PageViewService pageViewService;
<<<<<<< HEAD
    private final ProdukService produkService; // 2. TAMBAHKAN DI SINI (Otomatis terhubung tanpa @Autowired)
=======
>>>>>>> f58bcb704ca04e133dbe4515d85768387958ed38

    @GetMapping("/")
    public String landingPage(Model model) {
        WisataInfo info = wisataInfoRepository.findById(1L)
                .orElseGet(WisataInfo::new);

        model.addAttribute("info", info);
        model.addAttribute("galeri", fotoGaleriRepository.findAllByOrderByIdDesc());
        model.addAttribute("fasilitas", fasilitasRepository.findAll());

<<<<<<< HEAD
        // 3. TAMBAHKAN DI SINI untuk mengambil data produk ke landing page (index.html)
        model.addAttribute("produk", produkService.ambilSemuaProduk());

=======
>>>>>>> f58bcb704ca04e133dbe4515d85768387958ed38
        pageViewService.recordVisit();

        return "index";
    }

    @PostMapping("/feedback")
    public String submitFeedback(@RequestParam String nama,
<<<<<<< HEAD
                                 @RequestParam String pesan) {
        Feedback feedback = new Feedback();
        feedback.setNama(nama);
=======
                                @RequestParam String email,
                                @RequestParam String pesan) {
        Feedback feedback = new Feedback();
        feedback.setNama(nama);
        feedback.setEmail(email);
>>>>>>> f58bcb704ca04e133dbe4515d85768387958ed38
        feedback.setIsi(pesan);
        feedbackRepository.save(feedback);
        return "redirect:/?feedback=success";
    }
<<<<<<< HEAD

    @GetMapping("/produk-umkm")
    public String halamanSemuaProduk(Model model) {
        // Mengambil semua produk dari database untuk ditampilkan ke pengunjung umum
        model.addAttribute("daftarProduk", produkService.ambilSemuaProduk());
        return "produk-umkm"; // Ini akan mengarah ke file templates/produk-umkm.html
    }

=======
>>>>>>> f58bcb704ca04e133dbe4515d85768387958ed38
}