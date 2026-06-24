package com.sipokar.webapp.controller.admin;

import com.sipokar.webapp.model.DataPengunjung;
import com.sipokar.webapp.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUmkm", dashboardService.totalUmkm());
        model.addAttribute("umkmTerdaftar", dashboardService.umkmTerdaftar());
        model.addAttribute("umkmPending", dashboardService.umkmPending());
        model.addAttribute("totalFeedback", dashboardService.totalFeedback());
        model.addAttribute("feedbackBelumDitindaklanjuti", dashboardService.feedbackBelumDitindaklanjuti());
        model.addAttribute("totalKunjunganWebsite", dashboardService.totalKunjunganWebsite());
        model.addAttribute("totalPengunjung", dashboardService.totalPengunjung());
        model.addAttribute("entriPengunjungHariIni", dashboardService.entriPengunjungHariIni());
        model.addAttribute("pengunjungHariIni", dashboardService.pengunjungHariIni());
        model.addAttribute("checkInTerbaru", dashboardService.checkInTerbaru());

        List<DataPengunjung> tren = dashboardService.trenPengunjung();

        List<String> labels = tren.stream()
                .map(d -> d.getTanggalKunjungan().toString())
                .collect(Collectors.toList());
        List<Integer> values = tren.stream()
                .map(DataPengunjung::getJumlahOrang)
                .collect(Collectors.toList());

        // Serialize manual tanpa ObjectMapper — hindari dependency injection issue
        String labelsJson = "[" + labels.stream()
                .map(s -> "\"" + s + "\"")
                .collect(Collectors.joining(",")) + "]";
        String valuesJson = "[" + values.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")) + "]";

        model.addAttribute("trenLabels", labelsJson);
        model.addAttribute("trenValues", valuesJson);

        return "admin/dashboard";
    }
}
