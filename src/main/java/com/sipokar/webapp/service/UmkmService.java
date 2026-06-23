package com.sipokar.webapp.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.repository.UmkmRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UmkmService {

    private final UmkmRepository umkmRepository;

    public Optional<Umkm> findById(Long id) {
        return umkmRepository.findById(id);
    }

}