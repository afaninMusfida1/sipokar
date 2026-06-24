package com.sipokar.webapp.controller;

import com.sipokar.webapp.model.User;
import com.sipokar.webapp.repository.UmkmRepository;
import com.sipokar.webapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final UmkmRepository umkmRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                            @RequestParam String password,
                            Model model) {
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username sudah digunakan, silakan pilih username lain.");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.UMKM);
        userRepository.save(user);

        return "redirect:/login?registered=true";
    }

    @GetMapping("/dashboard")
    public String redirectDashboard(Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return "redirect:/admin/dashboard";
        }

        User user = userRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new IllegalStateException("User tidak ditemukan"));

        boolean sudahDaftar = umkmRepository.findByUser_Id(user.getId()).isPresent();

        return sudahDaftar
            ? "redirect:/umkm/dashboard"
            : "redirect:/umkm/daftar";
    }
}
