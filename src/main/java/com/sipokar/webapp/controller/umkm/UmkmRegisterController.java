package com.sipokar.webapp.controller.umkm;

import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.model.User;
import com.sipokar.webapp.repository.UmkmRepository;
import com.sipokar.webapp.service.UmkmService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/umkm/daftar")
@RequiredArgsConstructor
public class UmkmRegisterController {

    private final UmkmRepository umkmRepository;
    private final UmkmService umkmService;

    @GetMapping
    public String form(Authentication auth, Model model) {
        User user = umkmService.getCurrentUser(auth);

        // Jika sudah pernah daftar, langsung ke dashboard
        if (umkmRepository.findByUser_Id(user.getId()).isPresent()) {
            return "redirect:/umkm/dashboard";
        }

        model.addAttribute("umkm", new Umkm());
        return "umkm/daftar";
    }

    @PostMapping
    public String submit(@ModelAttribute Umkm umkm, Authentication auth) {
        User user = umkmService.getCurrentUser(auth);

        if (umkmRepository.findByUser_Id(user.getId()).isPresent()) {
            return "redirect:/umkm/dashboard";
        }

        umkm.setId(null);
        umkm.setStatus(Umkm.Status.PENDING);
        umkm.setUser(user);
        umkmRepository.save(umkm);

        return "redirect:/umkm/dashboard";
    }
}
