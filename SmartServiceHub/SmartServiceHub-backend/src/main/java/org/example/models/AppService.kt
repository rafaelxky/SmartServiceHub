package org.example.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.models.dto.AppServiceCreateDto
import org.example.models.dto.UserCreateDto
import java.io.Serializable

@Entity
@Table(name = "services")
class AppService(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var title: String = "",

    @Column(nullable = false)
    var content: String = "",

    @Column(nullable = false)
    var userId: Long? = null
) : Serializable {


    companion object {
        @JvmStatic
        fun fromCreateDto(appServiceCreateDto: AppServiceCreateDto, user: AppUser): AppService{
            return AppService(null, appServiceCreateDto.title, appServiceCreateDto.content, user.id)
        }
    }
}
