package com.sipokar.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sipokar.webapp.model.WisataInfo;

@Repository
public interface WisataInfoRepository extends JpaRepository<WisataInfo, Long> {
}