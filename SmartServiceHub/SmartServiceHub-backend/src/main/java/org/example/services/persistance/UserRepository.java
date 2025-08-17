package org.example.services.persistance;

import org.example.models.AppService;
import org.example.models.AppUser;
import org.example.models.dto.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Page<AppUser> findAll(Pageable pageable);
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query(value = "SELECT DISTINCT * FROM users LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<AppUser> findUniqueUser(@Param("limit") int limit, @Param("offset") int offset);
}
