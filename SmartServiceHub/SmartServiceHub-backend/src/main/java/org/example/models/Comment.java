package org.example.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private String text;
    @Column(nullable = false, unique = false)
    private Long post_id;
    @Column(nullable = false, unique = false)
    private Long user_id;

}
