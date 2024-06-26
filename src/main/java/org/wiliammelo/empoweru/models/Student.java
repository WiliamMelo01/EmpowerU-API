package org.wiliammelo.empoweru.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Student{

    @Id
    @GeneratedValue(strategy =GenerationType.UUID )
    private UUID id;

    @OneToOne
    private User user;

}
