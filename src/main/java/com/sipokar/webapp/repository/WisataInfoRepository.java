package com.sipokar.webapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sipokar.webapp.model.User;

@Repository
public interface WisataInfoRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}