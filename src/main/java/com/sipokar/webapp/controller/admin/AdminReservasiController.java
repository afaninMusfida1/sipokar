package com.sipokar.webapp.controller.admin;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sipokar.webapp.model.Reservasi;
import com.sipokar.webapp.model.ReservasiTempat;
import com.sipokar.webapp.repository.ReservasiRepository;
import com.sipokar.webapp.repository.ReservasiTempatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
@RequestMapping("/admin/reservasi")
@RequiredArgsConstructor
public class AdminReservasiController {

    private final ReservasiTempatRepository tempatRepository;
    private final ReservasiRepository reservasiRepository;
    private final Cloudinary cloudinary;

    @GetMapping
    public String daftar(Model model) {
        model.addAttribute("daftarTempat", tempatRepository.findAll());
        model.addAttribute("daftarReservasi",
                reservasiRepository.findAllByOrderByCreatedAtDesc());

        model.addAttribute("tempat", new ReservasiTempat());
        model.addAttribute("reservasi", new Reservasi());

        return "admin/reservasi";
    }

    @PostMapping("/tempat")
    public String tambahTempat(
            @ModelAttribute ReservasiTempat tempat,
            @RequestParam("fotoFile") MultipartFile fotoFile) {

        try {
            if (!fotoFile.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(
                        fotoFile.getBytes(),
                        ObjectUtils.asMap(
                                "folder", "sipokar/reservasi"));

                tempat.setFotoUrl(
                        uploadResult.get("secure_url").toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        tempatRepository.save(tempat);

        return "redirect:/admin/reservasi";
    }

    @PostMapping("/tempat/{id}/update")
    public String updateTempat(
            @PathVariable Long id,
            @ModelAttribute ReservasiTempat tempat,
            @RequestParam("fotoFile") MultipartFile fotoFile) {

        ReservasiTempat existing = tempatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tempat tidak ditemukan"));

        existing.setNama(tempat.getNama());
        existing.setDeskripsi(tempat.getDeskripsi());
        existing.setKapasitas(tempat.getKapasitas());

        try {
            if (!fotoFile.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(
                        fotoFile.getBytes(),
                        ObjectUtils.asMap(
                                "folder", "sipokar/reservasi"));

                existing.setFotoUrl(
                        uploadResult.get("secure_url").toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        tempatRepository.save(existing);

        return "redirect:/admin/reservasi";
    }

    @PostMapping("/tempat/{id}/hapus")
    public String hapusTempat(@PathVariable Long id) {
        tempatRepository.deleteById(id);

        return "redirect:/admin/reservasi";
    }

    @PostMapping("/booking")
    public String tambahReservasi(
            @ModelAttribute Reservasi reservasi,
            @RequestParam("tempatId") Long tempatId) {

        ReservasiTempat tempat = tempatRepository.findById(tempatId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tempat reservasi tidak ditemukan"));

        reservasi.setTempat(tempat);

        // otomatis booked
        reservasi.setStatus(Reservasi.Status.BOOKED);

        reservasiRepository.save(reservasi);

        return "redirect:/admin/reservasi";
    }

    @PostMapping("/booking/{id}/hapus")
    public String hapusReservasi(@PathVariable Long id) {
        reservasiRepository.deleteById(id);

        return "redirect:/admin/reservasi";
    }

    @PostMapping("/booking/{id}/update")
    public String updateReservasi(
            @PathVariable Long id,
            @ModelAttribute Reservasi reservasi,
            @RequestParam("tempatId") Long tempatId) {

        Reservasi existing = reservasiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Reservasi tidak ditemukan"));

        ReservasiTempat tempat = tempatRepository.findById(tempatId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tempat reservasi tidak ditemukan"));

        existing.setNamaPemesan(reservasi.getNamaPemesan());
        existing.setKontak(reservasi.getKontak());
        existing.setTempat(tempat);
        existing.setTanggal(reservasi.getTanggal());
        existing.setJumlahOrang(reservasi.getJumlahOrang());
        existing.setCatatan(reservasi.getCatatan());

        // tetap booked
        existing.setStatus(Reservasi.Status.BOOKED);

        reservasiRepository.save(existing);

        return "redirect:/admin/reservasi";
    }
}