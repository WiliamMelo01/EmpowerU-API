package org.wiliammelo.empoweru.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Entity class representing a Course.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String description;

    private int videosCount;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private List<Video> videos;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "Enrollments",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;

    @ElementCollection
    @CollectionTable(name = "course_tags", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "tag")
    private List<String> tags;

}
