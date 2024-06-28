package org.wiliammelo.empoweru.dtos.course;

import lombok.Getter;
import org.springframework.beans.BeanUtils;
import org.wiliammelo.empoweru.models.Course;

import java.util.List;
import java.util.UUID;

@Getter
public class CreateCourseDTO {

    private String title;

    private String description;

    private List<String> tags;

    private UUID professorId;

    public Course toCourse(){
        Course course = new Course();
        BeanUtils.copyProperties(this, course);
        return course;
    }

}
