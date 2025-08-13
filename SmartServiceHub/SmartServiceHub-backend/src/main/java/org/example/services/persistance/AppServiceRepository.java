package org.example.services.persistance;

import org.example.models.AppService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable; // ✅ CORRECT
import org.springframework.stereotype.Repository;

@Repository
public interface AppServiceRepository extends JpaRepository<AppService, Long> {
    Page<AppService> findAll(Pageable pageable);
}
