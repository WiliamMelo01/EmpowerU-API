package org.wiliammelo.empoweru.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Entity class representing a Professor.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 500)
    private String bio;

    private String imageUrl;

    @OneToOne
    private User user;
}
