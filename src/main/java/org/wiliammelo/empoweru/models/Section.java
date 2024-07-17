package org.wiliammelo.empoweru.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String description;

    @OneToMany(mappedBy = "section")
    private List<Video> videos;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToOne
    @JoinColumn(name = "evaluation_activity_id")
    private EvaluationActivity evaluationActivity;

}
