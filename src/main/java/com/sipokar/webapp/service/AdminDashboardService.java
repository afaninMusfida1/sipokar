package com.sipokar.webapp.service;

import com.sipokar.webapp.model.DataPengunjung;
import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.repository.DataPengunjungRepository;
import com.sipokar.webapp.repository.FeedbackRepository;
import com.sipokar.webapp.repository.UmkmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final UmkmRepository umkmRepository;
    private final FeedbackRepository feedbackRepository;
    private final DataPengunjungRepository dataPengunjungRepository;
    private final PageViewService pageViewService;

    public long totalUmkm() {
        return umkmRepository.count();
    }

    public long umkmTerdaftar() {
        return umkmRepository.countByStatus(Umkm.Status.VERIFIED);
    }

    public long umkmPending() {
        return umkmRepository.countByStatus(Umkm.Status.PENDING);
    }

    public long totalFeedback() {
        return feedbackRepository.count();
    }

    public long feedbackBelumDitindaklanjuti() {
        return feedbackRepository.countBySudahDitindaklanjutiFalse();
    }

    public long totalKunjunganWebsite() {
        return pageViewService.totalKunjungan();
    }

    public List<DataPengunjung> trenPengunjung() {
        List<DataPengunjung> data = dataPengunjungRepository.findDailyVisitorTotals().stream()
                .map(row -> {
                    DataPengunjung item = new DataPengunjung();
                    Object tanggal = row[0];
                    if (tanggal instanceof LocalDate) {
                        item.setTanggalKunjungan((LocalDate) tanggal);
                    } else if (tanggal instanceof java.sql.Date) {
                        item.setTanggalKunjungan(((java.sql.Date) tanggal).toLocalDate());
                    }
                    item.setJumlahOrang(((Number) row[1]).intValue());
                    return item;
                })
                .collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new));
        Collections.reverse(data); // urutkan dari lama -> baru untuk chart
        return data;
    }

    public long totalPengunjung() {
        return dataPengunjungRepository.count();
    }

    public long entriPengunjungHariIni() {
        return dataPengunjungRepository.countByTanggalKunjungan(LocalDate.now());
    }

    public int pengunjungHariIni() {
        return dataPengunjungRepository.findByTanggalKunjunganOrderByWaktuCheckInDesc(LocalDate.now())
                .stream()
                .mapToInt(p -> p.getJumlahOrang() == null ? 0 : p.getJumlahOrang())
                .sum();
    }

    public List<DataPengunjung> checkInTerbaru() {
        return dataPengunjungRepository.findTop50ByOrderByWaktuCheckInDesc().stream()
                .limit(10)
                .toList();
    }
}