package org.wiliammelo.empoweru.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String url;

    private String title;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

}
