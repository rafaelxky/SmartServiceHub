package org.example.services.persistance;

import org.example.App;
import org.example.models.AppService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface AppServiceRepository extends JpaRepository<AppService, Long> {
    List<AppService> findAll(Pageable pageable);
}
