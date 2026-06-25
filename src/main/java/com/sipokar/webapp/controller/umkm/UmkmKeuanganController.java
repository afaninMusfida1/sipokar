package com.sipokar.webapp.controller.umkm;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sipokar.webapp.model.Keuangan;
import com.sipokar.webapp.model.Retribusi;
import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.service.KeuanganService;
import com.sipokar.webapp.service.RetribusiService;
import com.sipokar.webapp.service.UmkmService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/umkm/keuangan")
@RequiredArgsConstructor
public class UmkmKeuanganController {

        private final KeuanganService keuanganService;
        private final RetribusiService retribusiService;
        private final UmkmService umkmService;

        @GetMapping
        public String halamanKeuangan(
                        Authentication auth,
                        Model model,
                        @RequestParam(required = false) String bulan) {

                Umkm umkm = umkmService.getCurrentUmkm(auth)
                                .orElseThrow(() -> new RuntimeException("UMKM tidak ditemukan"));

                Long umkmId = umkm.getId();

                YearMonth selectedMonth = (bulan != null && !bulan.isBlank())
                                ? YearMonth.parse(bulan)
                                : YearMonth.now();

                int year = selectedMonth.getYear();
                int month = selectedMonth.getMonthValue();

                List<Keuangan> transaksi = keuanganService.riwayat(umkmId, year, month);

                List<Retribusi> retribusiList = retribusiService.findByUmkmId(umkmId);

                model.addAttribute("umkm", umkm);
                model.addAttribute("transaksi", transaksi);
                model.addAttribute("retribusiList", retribusiList);
                model.addAttribute("totalPemasukan",
                                keuanganService.totalPemasukan(umkmId, year, month));
                model.addAttribute("totalPengeluaran",
                                keuanganService.totalPengeluaran(umkmId, year, month));
                model.addAttribute("saldo",
                                keuanganService.saldo(umkmId, year, month));

                model.addAttribute("bulan", selectedMonth.toString());

                return "umkm/keuangan";
        }

        @PostMapping("/transaksi")
        public String simpanTransaksi(
                        Authentication auth,
                        @RequestParam LocalDate tanggal,
                        @RequestParam String keterangan,
                        @RequestParam Keuangan.Tipe tipe,
                        @RequestParam BigDecimal nominal) {

                Umkm umkm = umkmService.getCurrentUmkm(auth)
                                .orElseThrow(() -> new RuntimeException("UMKM tidak ditemukan"));

                Keuangan transaksi = new Keuangan();
                transaksi.setUmkm(umkm);
                transaksi.setTanggal(tanggal);
                transaksi.setKeterangan(keterangan);
                transaksi.setTipe(tipe);
                transaksi.setNominal(nominal);

                keuanganService.save(transaksi);

                return "redirect:/umkm/keuangan?bulan=" + YearMonth.from(tanggal);
        }

        @PostMapping("/edit/{id}")
        public String updateTransaksi(
                        Authentication auth,
                        @PathVariable Long id,
                        @RequestParam LocalDate tanggal,
                        @RequestParam String keterangan,
                        @RequestParam Keuangan.Tipe tipe,
                        @RequestParam BigDecimal nominal) {

                Umkm umkm = umkmService.getCurrentUmkm(auth)
                                .orElseThrow(() -> new RuntimeException("UMKM tidak ditemukan"));

                Keuangan transaksi = keuanganService.findById(id)
                                .orElseThrow(() -> new RuntimeException("Transaksi tidak ditemukan"));

                if (!transaksi.getUmkm().getId().equals(umkm.getId())) {
                        throw new RuntimeException("Akses ditolak");
                }

                transaksi.setTanggal(tanggal);
                transaksi.setKeterangan(keterangan);
                transaksi.setTipe(tipe);
                transaksi.setNominal(nominal);

                keuanganService.save(transaksi);

                return "redirect:/umkm/keuangan?bulan=" + YearMonth.from(tanggal);
        }

        @GetMapping("/export")
        public void exportExcel(
                        Authentication auth,
                        @RequestParam(required = false) String bulan,
                        HttpServletResponse response) throws IOException {

                Umkm umkm = umkmService.getCurrentUmkm(auth)
                                .orElseThrow(() -> new RuntimeException("UMKM tidak ditemukan"));

                YearMonth selectedMonth = (bulan != null && !bulan.isBlank())
                                ? YearMonth.parse(bulan)
                                : YearMonth.now();

                List<Keuangan> transaksi = keuanganService.riwayat(
                                umkm.getId(),
                                selectedMonth.getYear(),
                                selectedMonth.getMonthValue());

                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Keuangan");

                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Tanggal");
                header.createCell(1).setCellValue("Keterangan");
                header.createCell(2).setCellValue("Tipe");
                header.createCell(3).setCellValue("Nominal");

                int rowNum = 1;

                for (Keuangan k : transaksi) {
                        Row row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(k.getTanggal().toString());
                        row.createCell(1).setCellValue(k.getKeterangan());
                        row.createCell(2).setCellValue(k.getTipe().toString());
                        row.createCell(3).setCellValue(k.getNominal().doubleValue());
                }

                response.setContentType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

                response.setHeader(
                                "Content-Disposition",
                                "attachment; filename=laporan-keuangan.xlsx");

                workbook.write(response.getOutputStream());
                workbook.close();
        }
}