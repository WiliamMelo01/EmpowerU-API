package org.wiliammelo.empoweru.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Professor{

    @Id
    @GeneratedValue(strategy =GenerationType.UUID )
    private UUID id;

    private String bio;

    private String imageUrl;

    @OneToOne
    private User user;
}
