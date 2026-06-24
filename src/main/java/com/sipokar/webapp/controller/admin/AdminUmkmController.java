package com.sipokar.webapp.controller.admin;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sipokar.webapp.model.Keuangan;
import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.repository.KeuanganRepository;
import com.sipokar.webapp.repository.UmkmRepository;
import com.sipokar.webapp.service.UmkmService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/umkm")
@RequiredArgsConstructor
public class AdminUmkmController {

    private final UmkmRepository umkmRepository;
    private final KeuanganRepository keuanganRepository; 
    private final UmkmService umkmService;

    // method yang sudah ada
    @GetMapping
    public String daftarUmkm(Model model) {
        model.addAttribute("daftarUmkm", umkmRepository.findAll());
        return "admin/umkm";
    }

    @GetMapping("/{id}")
    public String detailUmkm(@PathVariable Long id, Model model) {
        Umkm umkm = umkmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UMKM tidak ditemukan"));
        model.addAttribute("umkm", umkm);
        return "admin/umkm-detail";
    }

    @PostMapping("/{id}/verifikasi")
    public String verifikasi(@PathVariable Long id) {
        Umkm umkm = umkmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UMKM tidak ditemukan"));
        umkm.setStatus(Umkm.Status.VERIFIED);
        umkmRepository.save(umkm);
        return "redirect:/admin/umkm";
    }

    @PostMapping("/{id}/tolak")
    public String tolak(@PathVariable Long id) {
        Umkm umkm = umkmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UMKM tidak ditemukan"));
        umkm.setStatus(Umkm.Status.REJECTED);
        umkmRepository.save(umkm);
        return "redirect:/admin/umkm";
    }

    @PostMapping("/{id}/review")
    public String prosesReviewUmkm(
            @PathVariable Long id, 
            @RequestParam String action, 
            @RequestParam(required = false) String catatanReview) {
        
        // 1. Cari UMKM berdasarkan ID dari URL
        Umkm umkm = umkmRepository.findById(id) // Diseragamkan pakai umkmRepository
                .orElseThrow(() -> new IllegalArgumentException("UMKM tidak ditemukan"));
        
        // 2. Cek tombol apa yang diklik admin (VERIFY, REVISION, atau REJECT)
        switch (action) {
            case "VERIFY":
                umkm.setStatus(Umkm.Status.VERIFIED);
                umkm.setCatatanReview(null); // Bersihkan catatan kalau disetujui
                break;
            case "REVISION":
                umkm.setStatus(Umkm.Status.REVISION);
                umkm.setCatatanReview(catatanReview); // Simpan pesan revisi dari admin
                break;
            case "REJECT":
                umkm.setStatus(Umkm.Status.REJECTED);
                umkm.setCatatanReview(catatanReview); // Simpan alasan penolakan
                break;
        }
        
        // 3. Simpan perubahan ke database
        umkmRepository.save(umkm); // Diseragamkan pakai umkmRepository
        
        // 4. Redirect kembali ke halaman detail UMKM
        return "redirect:/admin/umkm/" + id;
    }

    // ==================== TAMBAHAN UNTUK KEUANGAN ====================

    // Menampilkan daftar transaksi keuangan untuk UMKM tertentu
    @GetMapping("/{id}/keuangan")
    public String daftarKeuangan(@PathVariable Long id, Model model) {
        Umkm umkm = umkmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UMKM tidak ditemukan"));
        model.addAttribute("umkm", umkm);
        model.addAttribute("daftarKeuangan", keuanganRepository.findByUmkm_IdOrderByTanggalDesc(id));
        // tambahkan enum untuk dropdown di form
        model.addAttribute("tipeOptions", Keuangan.Tipe.values());
        model.addAttribute("kategoriOptions", Keuangan.Kategori.values());
        return "admin/umkm-keuangan"; // view baru untuk daftar keuangan
    }

    // Menambah transaksi keuangan (dari form)
    @PostMapping("/{id}/keuangan/tambah")
    public String tambahKeuangan(@PathVariable Long id,
                                 @RequestParam LocalDate tanggal,
                                 @RequestParam String keterangan,
                                 @RequestParam Keuangan.Tipe tipe,
                                 @RequestParam BigDecimal nominal,
                                 @RequestParam Keuangan.Kategori kategori,
                                 RedirectAttributes redirectAttributes) {
        Umkm umkm = umkmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UMKM tidak ditemukan"));

        Keuangan keuangan = new Keuangan();
        keuangan.setTanggal(tanggal);
        keuangan.setKeterangan(keterangan);
        keuangan.setTipe(tipe);
        keuangan.setNominal(nominal);
        keuangan.setKategori(kategori);
        keuangan.setUmkm(umkm);

        keuanganRepository.save(keuangan);
        redirectAttributes.addFlashAttribute("success", "Transaksi berhasil ditambahkan.");
        return "redirect:/admin/umkm/" + id + "/keuangan";
    }

    // Menghapus transaksi keuangan
    @PostMapping("/{id}/keuangan/hapus/{keuanganId}")
    public String hapusKeuangan(@PathVariable Long id,
                                @PathVariable Long keuanganId,
                                RedirectAttributes redirectAttributes) {
        // validasi bahwa keuangan milik UMKM yang sesuai
        Keuangan keuangan = keuanganRepository.findById(keuanganId)
                .orElseThrow(() -> new IllegalArgumentException("Transaksi tidak ditemukan"));
        if (!keuangan.getUmkm().getId().equals(id)) {
            redirectAttributes.addFlashAttribute("error", "Transaksi tidak terhubung dengan UMKM ini.");
            return "redirect:/admin/umkm/" + id + "/keuangan";
        }
        keuanganRepository.delete(keuangan);
        redirectAttributes.addFlashAttribute("success", "Transaksi berhasil dihapus.");
        return "redirect:/admin/umkm/" + id + "/keuangan";
    }
}