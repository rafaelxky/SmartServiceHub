package org.example.services.persistance;

import org.example.models.AppService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<AppService, Long> {
}
