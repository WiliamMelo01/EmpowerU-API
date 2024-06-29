package org.wiliammelo.empoweru.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.dtos.course.CourseDTO;
import org.wiliammelo.empoweru.dtos.course.CreateCourseDTO;
import org.wiliammelo.empoweru.dtos.course.UpdateCourseDTO;
import org.wiliammelo.empoweru.exceptions.CourseNotFoundException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.mappers.CourseMapper;
import org.wiliammelo.empoweru.models.Course;
import org.wiliammelo.empoweru.models.Professor;
import org.wiliammelo.empoweru.repositories.CourseRepository;
import org.wiliammelo.empoweru.repositories.ProfessorRepository;
import org.wiliammelo.empoweru.repositories.VideoRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Transactional
    public CourseDTO create(CreateCourseDTO createCourseDTO) throws UserNotFoundException {
        Professor professor = this.professorRepository.findById(createCourseDTO.getProfessorId()).orElseThrow(UserNotFoundException::new);

        Course course = CourseMapper.INSTANCE.toCourse(createCourseDTO);
        course.setProfessor(professor);
       return CourseMapper.INSTANCE.toCourseDto(this.courseRepository.save(course));
    }

    public List<CourseDTO> findAll(){
        List<Course> courseList = (List<Course>) this.courseRepository.findAll();
        return courseList.stream().map(CourseMapper.INSTANCE::toCourseDto).toList();
    }

    public CourseDTO findById(UUID id)throws CourseNotFoundException {
        Course course = this.courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
        return CourseMapper.INSTANCE.toCourseDto(course);
    }

    public List<CourseDTO> findByTitle(String title){
        List<Course> courseList = this.courseRepository.findByTitleContainingIgnoreCase(title);
        return courseList.stream().map(CourseMapper.INSTANCE::toCourseDto).toList();
    }


    public List<CourseDTO> findByTags(List<String> tags){
        List<String> tagsInLowerCase = tags.stream().map(String::toLowerCase).toList();
        List<Course> courseList = this.courseRepository.findByTagsContainingIgnoreCase(tagsInLowerCase);
        return courseList.stream().map(CourseMapper.INSTANCE::toCourseDto).toList();
    }

    public List<CourseDTO> findByTitleAndTags(String title, List<String> tags){
        List<String> tagsInLowerCase = tags.stream().map(String::toLowerCase).toList();
        List<Course> courseList =  this.courseRepository.findByTitleContainingIgnoreCaseAndTagsContainingIgnoreCase(title, tagsInLowerCase);
        return courseList.stream().map(CourseMapper.INSTANCE::toCourseDto).toList();
    }

    public CourseDTO update(UUID id, UpdateCourseDTO updateCourseDTO)throws CourseNotFoundException {
        Course course = this.courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);

        course.setDescription(updateCourseDTO.getDescription());
        course.setTitle(updateCourseDTO.getTitle());
        course.setTags(updateCourseDTO.getTags());

        return CourseMapper.INSTANCE.toCourseDto(this.courseRepository.save(course));
    }

    @Transactional
    public String delete(UUID id) throws CourseNotFoundException{
        Course course = this.courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
        this.videoRepository.deleteAllByCourse(course);
        this.courseRepository.delete(course);
        return "Course with id: "+ id + " deleted successfully.";
    }

}
