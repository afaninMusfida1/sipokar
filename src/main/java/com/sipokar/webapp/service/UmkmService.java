package com.sipokar.webapp.service;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.sipokar.webapp.model.Umkm;
import com.sipokar.webapp.model.User;
import com.sipokar.webapp.repository.UmkmRepository;
import com.sipokar.webapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UmkmService {

    private final UmkmRepository umkmRepository;
    private final UserRepository userRepository;  // pastikan ada

    public Optional<Umkm> findById(Long id) {
        return umkmRepository.findById(id);
    }

    public Optional<Umkm> getCurrentUmkm(Authentication authentication) {
        if (authentication == null) return Optional.empty();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .flatMap(user -> umkmRepository.findByUser_Id(user.getId()));
    }

    public Optional<User> getCurrentUser(Authentication authentication) {
        if (authentication == null) return Optional.empty();
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

}