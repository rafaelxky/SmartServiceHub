package org.example.services.persistance;

import org.example.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAll(Pageable pageable);

    @Query(value = """
        SELECT *
        FROM comments c
        WHERE c.post_id = :postId
        AND c.content IN (
            SELECT DISTINCT content FROM comments WHERE post_id = :postId
        )
        LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Comment> findUniqueCommentsByPostId(@Param("postId") long postId, @Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT * FROM comments WHERE post_id = :postId", nativeQuery = true)
    List<Comment> findCommentsByPostId(@Param("postId") Long postId);
}
