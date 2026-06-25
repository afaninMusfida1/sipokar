package com.sipokar.webapp.controller.admin;

import com.sipokar.webapp.model.Reservasi;
import com.sipokar.webapp.model.ReservasiTempat;
import com.sipokar.webapp.repository.ReservasiRepository;
import com.sipokar.webapp.repository.ReservasiTempatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/reservasi")
@RequiredArgsConstructor
public class AdminReservasiController {

    private final ReservasiTempatRepository tempatRepository;
    private final ReservasiRepository reservasiRepository;

    @GetMapping
    public String daftar(Model model) {
        model.addAttribute("daftarTempat", tempatRepository.findAll());
        model.addAttribute("daftarReservasi", reservasiRepository.findAllByOrderByCreatedAtDesc());
        model.addAttribute("tempat", new ReservasiTempat());
        model.addAttribute("reservasi", new Reservasi());
        model.addAttribute("statusOptions", Reservasi.Status.values());
        return "admin/reservasi";
    }

    @PostMapping("/tempat")
    public String tambahTempat(@ModelAttribute ReservasiTempat tempat) {
        tempat.setId(null);
        tempatRepository.save(tempat);
        return "redirect:/admin/reservasi";
    }

    @PostMapping("/tempat/{id}/hapus")
    public String hapusTempat(@PathVariable Long id) {
        tempatRepository.deleteById(id);
        return "redirect:/admin/reservasi";
    }

    @PostMapping("/tempat/{id}/update")
    public String updateTempat(@PathVariable Long id, @ModelAttribute ReservasiTempat tempat) {
        ReservasiTempat existing = tempatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tempat reservasi tidak ditemukan"));
        existing.setNama(tempat.getNama());
        existing.setDeskripsi(tempat.getDeskripsi());
        existing.setLokasi(tempat.getLokasi());
        existing.setKapasitas(tempat.getKapasitas());
        existing.setFotoUrl(tempat.getFotoUrl());
        existing.setInfoTambahan(tempat.getInfoTambahan());
        tempatRepository.save(existing);
        return "redirect:/admin/reservasi";
    }

    @PostMapping("/booking")
    public String tambahReservasi(@ModelAttribute Reservasi reservasi,
                                  @RequestParam("tempatId") Long tempatId) {
        ReservasiTempat tempat = tempatRepository.findById(tempatId)
                .orElseThrow(() -> new IllegalArgumentException("Tempat reservasi tidak ditemukan"));
        reservasi.setTempat(tempat);

        if (reservasi.getStatus() == null) {
            reservasi.setStatus(Reservasi.Status.BOOKED);
        }
        reservasiRepository.save(reservasi);
        return "redirect:/admin/reservasi";
    }

    @PostMapping("/booking/{id}/hapus")
    public String hapusReservasi(@PathVariable Long id) {
        reservasiRepository.deleteById(id);
        return "redirect:/admin/reservasi";
    }

    @PostMapping("/booking/{id}/update")
    public String updateReservasi(@PathVariable Long id,
                                  @ModelAttribute Reservasi reservasi,
                                  @RequestParam("tempatId") Long tempatId) {
        Reservasi existing = reservasiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservasi tidak ditemukan"));
        ReservasiTempat tempat = tempatRepository.findById(tempatId)
                .orElseThrow(() -> new IllegalArgumentException("Tempat reservasi tidak ditemukan"));

        existing.setNamaPemesan(reservasi.getNamaPemesan());
        existing.setKontak(reservasi.getKontak());
        existing.setTempat(tempat);
        existing.setTanggal(reservasi.getTanggal());
        existing.setJumlahOrang(reservasi.getJumlahOrang());
        existing.setStatus(reservasi.getStatus() != null ? reservasi.getStatus() : Reservasi.Status.BOOKED);
        existing.setCatatan(reservasi.getCatatan());
        reservasiRepository.save(existing);
        return "redirect:/admin/reservasi";
    }
}
