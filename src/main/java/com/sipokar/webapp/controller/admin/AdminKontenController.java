package com.sipokar.webapp.controller.admin;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;   // <- tambahkan ini
import org.springframework.web.bind.annotation.RestController;

import com.sipokar.webapp.model.WisataInfo;
import com.sipokar.webapp.repository.WisataInfoRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/konten")
@RequiredArgsConstructor
public class AdminKontenController {

    private final WisataInfoRepository wisataInfoRepository;

    @GetMapping
    public List<WisataInfo> getAll() {
        return wisataInfoRepository.findAll();
    }

    @PostMapping
    public WisataInfo create(@RequestBody WisataInfo wisataInfo) {
        return wisataInfoRepository.save(wisataInfo);
    }
}