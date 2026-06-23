package com.sipokar.webapp.service;

import com.sipokar.webapp.model.DataPengunjung;
import com.sipokar.webapp.repository.DataPengunjungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PengunjungService {

    private final DataPengunjungRepository dataPengunjungRepository;

    public DataPengunjung simpan(DataPengunjung dataPengunjung) {
        if (dataPengunjung.getTanggalKunjungan() == null) {
            dataPengunjung.setTanggalKunjungan(LocalDate.now());
        }
        if (dataPengunjung.getJumlahOrang() == null || dataPengunjung.getJumlahOrang() < 1) {
            dataPengunjung.setJumlahOrang(1);
        }
        dataPengunjung.setWaktuCheckIn(LocalDateTime.now());
        dataPengunjung.setId(null);

        return dataPengunjungRepository.save(dataPengunjung);
    }

    public List<DataPengunjung> riwayatTerbaru() {
        return dataPengunjungRepository.findTop50ByOrderByWaktuCheckInDesc();
    }

    public long totalEntri() {
        return dataPengunjungRepository.count();
    }

    public long entriHariIni() {
        return dataPengunjungRepository.countByTanggalKunjungan(LocalDate.now());
    }

    public int jumlahOrangHariIni() {
        return dataPengunjungRepository.findByTanggalKunjunganOrderByWaktuCheckInDesc(LocalDate.now())
                .stream()
                .mapToInt(DataPengunjung::getJumlahOrang)
                .sum();
    }
}
