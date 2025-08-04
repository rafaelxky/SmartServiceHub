package org.example.services.persistance;

import org.example.models.AppUser;
import org.example.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Page<AppUser> findAll(Pageable pageable);
}
