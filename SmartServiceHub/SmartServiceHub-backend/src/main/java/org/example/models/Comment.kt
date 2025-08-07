package org.example.models

import javax.persistence.*

@Entity
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var text: String = "",

    @Column(nullable = false)
    var postId: Long? = null,

    @Column(nullable = false)
    var userId: Long? = null,
)
