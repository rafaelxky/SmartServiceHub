package org.example.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.models.dto.ServicePostCreateDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "services")
@EntityListeners(AuditingEntityListener::class)
class ServicePost(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var title: String = "",

    @Column(nullable = false)
    var content: String = "",

    @Column(nullable = false)
    var creatorId: Long? = null,

    @Column(nullable = false, updatable = false)
    @CreatedDate
    var timestamp: LocalDateTime? = null,
) : Serializable {


    companion object {
        @JvmStatic
        fun fromCreateDto(servicePostCreateDto: ServicePostCreateDto, user: AppUser): ServicePost{
            return ServicePost(null, servicePostCreateDto.title, servicePostCreateDto.content, user.id)
        }
    }
}
