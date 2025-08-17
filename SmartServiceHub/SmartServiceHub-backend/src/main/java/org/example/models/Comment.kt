package org.example.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.models.dto.CommentCreateDto

@Entity
@Table(name = "comments")
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var content: String = "",

    @Column(nullable = false)
    var postId: Long? = null,

    @Column(nullable = false)
    var userId: Long? = null,
) {
    companion object {
        @JvmStatic
        fun formCreateDto(commentCreateDto: CommentCreateDto, user: AppUser): Comment{
            return Comment(null, commentCreateDto.getContent(), commentCreateDto.getPostId(), user.id)
        }
    }
}
