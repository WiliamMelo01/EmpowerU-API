package org.wiliammelo.empoweru.dtos.course;

import lombok.Getter;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.wiliammelo.empoweru.models.Course;

import java.util.List;

@Getter
public class UpdateCourseDTO {

    private String title;

    private String description;

    private List<String> tags;

    public Course toCourse(){
        Course course = new Course();
        BeanUtils.copyProperties(this, course);
        return course;
    }

}
