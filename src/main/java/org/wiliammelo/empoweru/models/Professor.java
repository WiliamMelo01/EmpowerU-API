package org.wiliammelo.empoweru.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Professor{

    @Id
    @GeneratedValue(strategy =GenerationType.UUID )
    private UUID id;

    private String bio;

    private String imageUrl;

    @OneToOne
    private User user;
}
