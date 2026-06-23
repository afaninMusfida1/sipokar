package com.sipokar.webapp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sipokar.webapp.model.Retribusi;
import com.sipokar.webapp.repository.RetribusiRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RetribusiService {

    private final RetribusiRepository retribusiRepository;

    // Method yang dipanggil controller
    public List<Retribusi> findByUmkmAndTanggalBetween(Long umkmId, LocalDate start, LocalDate end) {
        return retribusiRepository.findByUmkmIdAndTanggalBetween(umkmId, start, end);
    }

    public Retribusi save(Retribusi retribusi) {
        return retribusiRepository.save(retribusi);
    }

    public List<Retribusi> findByUmkmId(Long umkmId) {
        return retribusiRepository.findByUmkmId(umkmId); 
    }
    
}