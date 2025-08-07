package org.example.models

import javax.persistence.*
import java.io.Serializable

@Entity
@Table(name = "app_service")
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
) : Serializable
