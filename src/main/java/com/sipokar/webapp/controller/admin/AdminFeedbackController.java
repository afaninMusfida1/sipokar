package com.sipokar.webapp.controller.admin;

import com.sipokar.webapp.model.Feedback;
import com.sipokar.webapp.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/feedback")
@RequiredArgsConstructor
public class AdminFeedbackController {

    private final FeedbackRepository feedbackRepository;

    @GetMapping
    public String daftarFeedback(Model model) {
        model.addAttribute("daftarFeedback", feedbackRepository.findAllByOrderByCreatedAtDesc());
        return "admin/feedback";
    }

    @PostMapping("/{id}/respons")
    public String tambahRespons(@PathVariable Long id, @RequestParam String catatanRespons) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback tidak ditemukan"));
        feedback.setCatatanRespons(catatanRespons);
        feedback.setSudahDitindaklanjuti(true);
        feedbackRepository.save(feedback);
        return "redirect:/admin/feedback";
    }
}
