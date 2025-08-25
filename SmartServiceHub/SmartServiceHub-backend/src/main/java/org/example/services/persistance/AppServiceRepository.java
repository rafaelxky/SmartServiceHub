package org.example.services.persistance;

import org.example.models.ServicePost;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable; // ✅ CORRECT
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppServiceRepository extends JpaRepository<ServicePost, Long> {
    Page<ServicePost> findAll(Pageable pageable);

    @Query(value = "SELECT DISTINCT * FROM services ORDER BY timestamp DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<ServicePost> findUniqueServicePost(@Param("limit") int limit, @Param("offset") int offset);
}
