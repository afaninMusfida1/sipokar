package com.sipokar.webapp.controller;

import com.sipokar.webapp.model.User;
import com.sipokar.webapp.repository.UmkmRepository;
import com.sipokar.webapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
=======
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
>>>>>>> f58bcb704ca04e133dbe4515d85768387958ed38
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
<<<<<<< HEAD
=======
    private final JavaMailSender mailSender;
>>>>>>> f58bcb704ca04e133dbe4515d85768387958ed38

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
<<<<<<< HEAD
    public String register(@RequestParam String username,
                            @RequestParam String password,
                            Model model) {
=======
    public String register(@RequestParam String email,
                           @RequestParam String username,
                           @RequestParam String password,
                           Model model) {
>>>>>>> f58bcb704ca04e133dbe4515d85768387958ed38
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username sudah digunakan, silakan pilih username lain.");
            return "register";
        }

        User user = new User();
<<<<<<< HEAD
=======
        user.setEmail(email);
>>>>>>> f58bcb704ca04e133dbe4515d85768387958ed38
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.UMKM);
        userRepository.save(user);

        return "redirect:/login?registered=true";
    }

<<<<<<< HEAD
=======
    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Reset Password Akun Sipokar");
            message.setText("Halo,\n\nAnda menerima email ini karena ada permintaan untuk mereset password.\n" +
                            "Silakan klik tautan berikut untuk membuat password baru:\n" +
                            "http://localhost:8080/update-password?email=" + email);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/reset-password?error=true";
        }

        return "redirect:/reset-password?success=true";
    }

>>>>>>> f58bcb704ca04e133dbe4515d85768387958ed38
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
<<<<<<< HEAD
}
=======

    @GetMapping("/update-password")
public String updatePasswordPage(@RequestParam String email, Model model) {
    model.addAttribute("email", email);
    return "update-password";
}

@PostMapping("/update-password")
public String processUpdatePassword(@RequestParam String email,
                                    @RequestParam String password,
                                    Model model) {
    var userOpt = userRepository.findByEmail(email);
    if (userOpt.isEmpty()) {
        model.addAttribute("error", "Email tidak ditemukan.");
        model.addAttribute("email", email);
        return "update-password";
    }

    User user = userOpt.get();
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);

    return "redirect:/login?passwordUpdated=true";
}
}
>>>>>>> f58bcb704ca04e133dbe4515d85768387958ed38
