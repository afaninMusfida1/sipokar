package com.sipokar.webapp.repository;

import com.sipokar.webapp.model.WisataInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WisataInfoRepository extends JpaRepository<WisataInfo, Long> {
}
