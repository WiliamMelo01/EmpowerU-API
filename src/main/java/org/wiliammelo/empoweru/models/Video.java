package org.wiliammelo.empoweru.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String url;

    private String title;

    private double durationInSeconds;

    @Column(name = "display_order")
    private int displayOrder;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

}
