package org.example.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.models.dto.CommentCreateDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "comments")
@EntityListeners(AuditingEntityListener::class)
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var content: String = "",

    @Column(nullable = false)
    var postId: Long? = null,

    @Column(nullable = false)
    var creatorId: Long? = null,

    @Column(nullable = false, updatable = false)
    @CreatedDate
    var timestamp: LocalDateTime? = null,
) {
    companion object {
        @JvmStatic
        fun fromCreateDto(commentCreateDto: CommentCreateDto, user: AppUser): Comment{
            return Comment(null, commentCreateDto.content, commentCreateDto.postId, user.id)
        }
    }
}
