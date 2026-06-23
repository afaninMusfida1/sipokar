package com.sipokar.webapp.controller;

import com.sipokar.webapp.model.Feedback;
import com.sipokar.webapp.model.WisataInfo;
import com.sipokar.webapp.repository.FasilitasRepository;
import com.sipokar.webapp.repository.FeedbackRepository;
import com.sipokar.webapp.repository.FotoGaleriRepository;
import com.sipokar.webapp.repository.WisataInfoRepository;
import com.sipokar.webapp.service.PageViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PublicController {

    private final WisataInfoRepository wisataInfoRepository;
    private final FeedbackRepository feedbackRepository;
    private final FotoGaleriRepository fotoGaleriRepository;
    private final FasilitasRepository fasilitasRepository;
    private final PageViewService pageViewService;

    @GetMapping("/")
    public String landingPage(Model model) {
        WisataInfo info = wisataInfoRepository.findById(1L)
                .orElseGet(WisataInfo::new);

        model.addAttribute("info", info);
        model.addAttribute("galeri", fotoGaleriRepository.findAllByOrderByIdDesc());
        model.addAttribute("fasilitas", fasilitasRepository.findAll());

        pageViewService.recordVisit();

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
}