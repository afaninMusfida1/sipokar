package com.sipokar.webapp.service;

import com.sipokar.webapp.model.PageView;
import com.sipokar.webapp.repository.PageViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PageViewService {

    private final PageViewRepository pageViewRepository;

    public void recordVisit() {
        LocalDate today = LocalDate.now();
        PageView pv = pageViewRepository.findByTanggal(today)
                .orElseGet(() -> {
                    PageView p = new PageView();
                    p.setTanggal(today);
                    p.setJumlah(0L);
                    return p;
                });
        pv.setJumlah(pv.getJumlah() + 1);
        pageViewRepository.save(pv);
    }

    public long totalKunjungan() {
        Long total = pageViewRepository.totalKunjungan();
        return total == null ? 0L : total;
    }

    public long kunjunganHariIni() {
        return pageViewRepository.findByTanggal(LocalDate.now())
                .map(PageView::getJumlah)
                .orElse(0L);
    }
}