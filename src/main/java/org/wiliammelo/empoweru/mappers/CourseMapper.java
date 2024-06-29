package org.wiliammelo.empoweru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.wiliammelo.empoweru.dtos.course.CourseDTO;
import org.wiliammelo.empoweru.dtos.course.CreateCourseDTO;
import org.wiliammelo.empoweru.models.Course;

@Mapper
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    Course toCourse(CreateCourseDTO courseDTO);

    @Mapping(source = "professor.user.name", target = "professor.name")
    @Mapping(source = "professor.user.email", target = "professor.email")
    @Mapping(source = "professor.user.gender", target = "professor.gender")
    @Mapping(source = "professor.id", target = "professor.id")
    @Mapping(source = "professor.imageUrl", target = "professor.imageURL")
    @Mapping(source = "professor.bio", target = "professor.bio")
    CourseDTO toCourseDto(Course course);

}
