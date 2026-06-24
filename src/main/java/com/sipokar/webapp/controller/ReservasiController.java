package com.sipokar.webapp.controller;

import com.sipokar.webapp.model.Reservasi;
import com.sipokar.webapp.repository.ReservasiRepository;
import com.sipokar.webapp.repository.ReservasiTempatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ReservasiController {

    private final ReservasiTempatRepository tempatRepository;
    private final ReservasiRepository reservasiRepository;

    @GetMapping("/reservasi")
    public String reservasiPage(Model model) {
        model.addAttribute("daftarTempat", tempatRepository.findAll());

        Set<Long> reservedTempatIds = reservasiRepository
                .findByTanggalGreaterThanEqualOrderByTanggalAsc(LocalDate.now())
                .stream()
                .map(reservasi -> reservasi.getTempat().getId())
                .collect(Collectors.toSet());

        model.addAttribute("reservedTempatIds", reservedTempatIds);
        return "reservasi";
    }
}
