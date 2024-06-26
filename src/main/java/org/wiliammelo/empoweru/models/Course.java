package org.wiliammelo.empoweru.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @OneToMany(mappedBy = "course")
    private List<Video> videos;

    @ManyToMany
    @JoinTable(
            name = "Enrollments",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;

}
