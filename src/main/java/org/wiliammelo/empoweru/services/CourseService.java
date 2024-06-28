package org.wiliammelo.empoweru.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.dtos.course.CreateCourseDTO;
import org.wiliammelo.empoweru.dtos.course.UpdateCourseDTO;
import org.wiliammelo.empoweru.exceptions.CourseNotFoundException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.models.Course;
import org.wiliammelo.empoweru.models.Professor;
import org.wiliammelo.empoweru.repositories.CourseRepository;
import org.wiliammelo.empoweru.repositories.ProfessorRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Transactional
    public Course create(CreateCourseDTO createCourseDTO) throws UserNotFoundException {
        System.out.println(createCourseDTO.getProfessorId());
        Professor professor = this.professorRepository.findById(createCourseDTO.getProfessorId()).orElseThrow(UserNotFoundException::new);
        System.out.println(professor.toString());

        Course course = createCourseDTO.toCourse();
        course.setProfessor(professor);
       return this.courseRepository.save(course);
    }

    public List<Course> findAll(){
        return (List<Course>) this.courseRepository.findAll();
    }

    public Course findById(UUID id)throws CourseNotFoundException {
        return this.courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
    }

    public List<Course> findByTitle(String title){
        return this.courseRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Course> findByTags(List<String> tags){
        return this.courseRepository.findByTagsContainingIgnoreCase(tags.stream().map(String::toLowerCase).toList());
    }

    public List<Course> findByTitleAndTags(String title, List<String> tags){
        return this.courseRepository.findByTitleContainingIgnoreCaseAndTagsContainingIgnoreCase(title, tags.stream().map(String::toLowerCase).toList());
    }

    public Course update(UUID id, UpdateCourseDTO updateCourseDTO)throws CourseNotFoundException {
        Course course = this.courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);

        course.setDescription(updateCourseDTO.getDescription());
        course.setTitle(updateCourseDTO.getTitle());
        course.setTags(updateCourseDTO.getTags());

        return this.courseRepository.save(course);
    }

    public String delete(UUID id) throws CourseNotFoundException{
        Course course = this.courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
        this.courseRepository.delete(course);
        return "Course with id: "+ id + " deleted successfully.";
    }

}
