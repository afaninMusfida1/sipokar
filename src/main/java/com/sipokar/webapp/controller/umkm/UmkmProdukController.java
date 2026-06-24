package com.sipokar.webapp.controller.umkm;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sipokar.webapp.model.Produk;
import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.model.User;
import com.sipokar.webapp.repository.UmkmRepository;
import com.sipokar.webapp.repository.UserRepository;
import com.sipokar.webapp.service.ProdukService;

@Controller
@RequestMapping("/umkm/produk")
public class UmkmProdukController {

    @Autowired
    private ProdukService produkService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UmkmRepository umkmRepository;

    @Autowired
    private Cloudinary cloudinary;

    // RUTE UTAMA: Menampilkan daftar produk
    @GetMapping
    public String halamanKelolaProduk(Model model, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new IllegalStateException("User tidak ditemukan"));

        Umkm umkmLogin = umkmRepository.findByUser_Id(user.getId())
            .orElseThrow(() -> new IllegalStateException("UMKM belum terdaftar"));

        List<Produk> daftarProduk = produkService.ambilProdukPerUmkm(umkmLogin);

        model.addAttribute("daftarProduk", daftarProduk);
        model.addAttribute("produk", new Produk()); 
        model.addAttribute("isTambah", false);    

        return "umkm/kelola-produk";
    }

    // RUTE TAMBAH: Menampilkan form tambah
    @GetMapping("/tambah")
    public String halamanTambahProduk(Model model, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new IllegalStateException("User tidak ditemukan"));

        Umkm umkmLogin = umkmRepository.findByUser_Id(user.getId())
            .orElseThrow(() -> new IllegalStateException("UMKM belum terdaftar"));

        List<Produk> daftarProduk = produkService.ambilProdukPerUmkm(umkmLogin);
        model.addAttribute("daftarProduk", daftarProduk);
        model.addAttribute("produk", new Produk()); 
        model.addAttribute("isTambah", true);      

        return "umkm/kelola-produk"; 
    }

    // RUTE POST: Simpan produk dengan upload gambar ke Cloudinary
    @PostMapping("/tambah")
    public String simpanProduk(Produk produk, @RequestParam("fileGambar") MultipartFile fileGambar, Authentication auth, RedirectAttributes ra) {
        User user = userRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new IllegalStateException("User tidak ditemukan"));

        Umkm umkmLogin = umkmRepository.findByUser_Id(user.getId())
            .orElseThrow(() -> new IllegalStateException("UMKM belum terdaftar"));

        if (!fileGambar.isEmpty()) {
            try {
                // Upload ke Cloudinary
                Map uploadResult = cloudinary.uploader().upload(fileGambar.getBytes(), ObjectUtils.emptyMap());
                String urlGambar = uploadResult.get("secure_url").toString();
                produk.setGambar(urlGambar);
            } catch (IOException e) {
                e.printStackTrace();
                produk.setGambar("/images/produk/placeholder.jpg");
            }
        } else {
            // Jika tidak ada gambar, pakai default
            produk.setGambar("/images/produk/placeholder.jpg");
        }

        produk.setUmkm(umkmLogin);
        produkService.simpanProduk(produk);
        ra.addFlashAttribute("sukses", "Produk berhasil ditambahkan!");

        return "redirect:/umkm/produk"; 
    }

    // RUTE HAPUS
    @GetMapping("/hapus/{id}")
    public String hapusProduk(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            produkService.hapusProduk(id); 
            ra.addFlashAttribute("sukses", "Produk berhasil dihapus!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menghapus produk: " + e.getMessage());
        }
        return "redirect:/umkm/produk";
    }

    // RUTE EDIT
    @GetMapping("/edit/{id}")
    public String halamanEditProduk(@PathVariable("id") Long id, Model model, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new IllegalStateException("User tidak ditemukan"));

        Umkm umkmLogin = umkmRepository.findByUser_Id(user.getId())
            .orElseThrow(() -> new IllegalStateException("UMKM belum terdaftar"));

        List<Produk> daftarProduk = produkService.ambilProdukPerUmkm(umkmLogin);
        model.addAttribute("daftarProduk", daftarProduk);

        Produk produkYangDicari = produkService.ambilProdukBerdasarkanId(id);
        model.addAttribute("produk", produkYangDicari);
        model.addAttribute("isTambah", true); 
        
        return "umkm/kelola-produk";
    }
}