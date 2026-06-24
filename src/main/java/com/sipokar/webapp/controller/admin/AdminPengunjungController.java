package com.sipokar.webapp.controller.admin;

import com.sipokar.webapp.repository.DataPengunjungRepository;
import com.sipokar.webapp.service.PengunjungService;
import com.sipokar.webapp.service.QrCodeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequestMapping("/admin/pengunjung")
@RequiredArgsConstructor
public class AdminPengunjungController {

    private final PengunjungService pengunjungService;
    private final DataPengunjungRepository dataPengunjungRepository;
    private final QrCodeService qrCodeService;

    @GetMapping
    public String daftar(Model model) {
        model.addAttribute("daftarTamu", pengunjungService.riwayatTerbaru());
        model.addAttribute("entriHariIni", pengunjungService.entriHariIni());
        model.addAttribute("jumlahOrangHariIni", pengunjungService.jumlahOrangHariIni());
        return "admin/pengunjung";
    }

    @GetMapping("/qr")
    public String halamanQr(Model model, HttpServletRequest request) {
        String checkInUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(request.getContextPath() + "/form-kunjungan")
                .build()
                .toUriString();
        model.addAttribute("checkInUrl", checkInUrl);
        return "admin/pengunjung-qr";
    }

    @GetMapping("/qr/image")
    public ResponseEntity<byte[]> qrImage(HttpServletRequest request) {
        String checkInUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(request.getContextPath() + "/form-kunjungan")
                .build()
                .toUriString();
        byte[] png = qrCodeService.generateQrPng(checkInUrl, 400);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(png);
    }

    @PostMapping("/{id}/hapus")
    public String hapus(@PathVariable Long id) {
        dataPengunjungRepository.deleteById(id);
        return "redirect:/admin/pengunjung";
    }
}