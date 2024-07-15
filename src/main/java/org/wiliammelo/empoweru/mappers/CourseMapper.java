package org.wiliammelo.empoweru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.wiliammelo.empoweru.dtos.course.CourseDTO;
import org.wiliammelo.empoweru.dtos.course.CourseDetailedDTO;
import org.wiliammelo.empoweru.dtos.course.CreateCourseDTO;
import org.wiliammelo.empoweru.models.Course;

/**
 * Mapper interface for converting between Course entities and their DTO representations.
 * Utilizes MapStruct for mapping fields between source and target objects.
 */
@Mapper
public interface CourseMapper {

    /**
     * Instance of the CourseMapper for use where dependency injection is not available.
     */
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    /**
     * Converts a CreateCourseDTO to a Course entity.
     *
     * @param courseDTO The CreateCourseDTO to convert.
     * @return The converted Course entity.
     */
    Course toCourse(CreateCourseDTO courseDTO);

    /**
     * Converts a Course entity to a CourseDTO.
     *
     * @param course The Course entity to convert.
     * @return The converted CourseDTO.
     */
    @Mapping(source = "professor.user.name", target = "professor.name")
    @Mapping(source = "professor.user.email", target = "professor.email")
    @Mapping(source = "professor.user.gender", target = "professor.gender")
    @Mapping(source = "professor.id", target = "professor.id")
    @Mapping(source = "professor.imageUrl", target = "professor.imageURL")
    @Mapping(source = "professor.bio", target = "professor.bio")
    CourseDTO toCourseDto(Course course);

    @Mapping(source = "professor.user.name", target = "professor.name")
    @Mapping(source = "professor.user.email", target = "professor.email")
    @Mapping(source = "professor.user.gender", target = "professor.gender")
    @Mapping(source = "professor.id", target = "professor.id")
    @Mapping(source = "professor.imageUrl", target = "professor.imageURL")
    @Mapping(source = "professor.bio", target = "professor.bio")
    @Mapping(source = "sections", target = "sections")
    CourseDetailedDTO toCourseDetailedDto(Course course);

}
