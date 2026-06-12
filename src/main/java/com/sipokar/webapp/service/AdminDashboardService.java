package com.sipokar.webapp.service;

import com.sipokar.webapp.model.DataPengunjung;
import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.repository.DataPengunjungRepository;
import com.sipokar.webapp.repository.FeedbackRepository;
import com.sipokar.webapp.repository.UmkmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        List<DataPengunjung> data = dataPengunjungRepository.findTop30ByOrderByTanggalDesc();
        Collections.reverse(data); // urutkan dari lama -> baru untuk chart
        return data;
    }
}