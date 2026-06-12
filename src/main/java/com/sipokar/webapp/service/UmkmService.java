package com.sipokar.webapp.service;

import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.model.User;
import com.sipokar.webapp.repository.UmkmRepository;
import com.sipokar.webapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UmkmService {

    private final UserRepository userRepository;
    private final UmkmRepository umkmRepository;

    public User getCurrentUser(Authentication auth) {
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalStateException("User tidak ditemukan"));
    }

    public Umkm getCurrentUmkm(Authentication auth) {
        User user = getCurrentUser(auth);
        return umkmRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new IllegalStateException("Data UMKM belum didaftarkan"));
    }
}
