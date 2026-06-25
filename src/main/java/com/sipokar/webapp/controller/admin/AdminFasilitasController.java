package com.sipokar.webapp.controller.admin;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sipokar.webapp.model.Fasilitas;
import com.sipokar.webapp.repository.FasilitasRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/fasilitas")
@RequiredArgsConstructor
public class AdminFasilitasController {

    private final FasilitasRepository fasilitasRepository;
    private final Cloudinary cloudinary;

    @GetMapping
    public String daftar(Model model) {
        model.addAttribute("daftarFasilitas", fasilitasRepository.findAll());
        model.addAttribute("fasilitas", new Fasilitas());
        return "admin/fasilitas";
    }

    @PostMapping
    public String tambah(@ModelAttribute Fasilitas fasilitas,
                          @RequestParam("file") MultipartFile file) throws IOException {

        fasilitas.setId(null);

        if (!file.isEmpty()) {
            @SuppressWarnings("unchecked")
            Map<String, Object> hasil = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "fasilitas")
            );
            fasilitas.setFoto((String) hasil.get("secure_url"));
        }

        fasilitasRepository.save(fasilitas);
        return "redirect:/admin/fasilitas";
    }

    @PostMapping("/{id}/hapus")
    public String hapus(@PathVariable Long id) throws IOException {
        Optional<Fasilitas> existing = fasilitasRepository.findById(id);

        existing.ifPresent(f -> {
            String foto = f.getFoto();
            if (foto != null && foto.startsWith("http")) {
                String publicId = extractPublicId(foto);
                try {
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                } catch (IOException e) {
                }
            }
        });

        fasilitasRepository.deleteById(id);
        return "redirect:/admin/fasilitas";
    }

    private String extractPublicId(String url) {
        String tanpaQuery = url.split("\\?")[0];
        String setelahUpload = tanpaQuery.substring(tanpaQuery.indexOf("/upload/") + "/upload/".length());
        String[] segmen = setelahUpload.split("/");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < segmen.length; i++) {
            if (i == 0 && segmen[i].matches("v\\d+")) continue;
            if (sb.length() > 0) sb.append("/");
            sb.append(segmen[i]);
        }
        String hasil = sb.toString();
        int titikTerakhir = hasil.lastIndexOf('.');
        return titikTerakhir > 0 ? hasil.substring(0, titikTerakhir) : hasil;
    }
}