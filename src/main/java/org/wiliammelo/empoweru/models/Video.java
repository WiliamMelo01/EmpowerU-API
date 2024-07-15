package org.wiliammelo.empoweru.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Entity class representing a Video.
 */
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
    @JoinColumn(name = "section_id")
    private Section section;

}
