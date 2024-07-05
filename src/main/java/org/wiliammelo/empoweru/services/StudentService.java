package org.wiliammelo.empoweru.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.dtos.student.StudentDTO;
import org.wiliammelo.empoweru.dtos.student.UpdateStudentDTO;
import org.wiliammelo.empoweru.exceptions.StudentNotFoundException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.mappers.StudentMapper;
import org.wiliammelo.empoweru.models.Student;
import org.wiliammelo.empoweru.models.User;
import org.wiliammelo.empoweru.repositories.StudentRepository;

import java.util.List;
import java.util.UUID;

/**
 * Service class for managing students.
 * <p>
 * This class provides services for creating, retrieving, updating, and deleting students. It interacts with the {@link StudentRepository}
 * and {@link UserService} to perform operations on {@link Student} entities.
 * </p>
 */
@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserService userService;

    /**
     * Finds a student by ID.
     *
     * @param id The UUID of the student to find.
     * @return The found student as a {@link StudentDTO}.
     * @throws StudentNotFoundException if the student is not found.
     */
    @Cacheable(value = "student", key = "#id")
    public StudentDTO findById(UUID id) throws StudentNotFoundException {
        Student student = this.studentRepository.findById(id)
                .orElseThrow(StudentNotFoundException::new);
        return StudentMapper.INSTANCE.toStudentDTO(student);
    }

    /**
     * Retrieves all students.
     *
     * @return A list of all students as {@link StudentDTO}s.
     */
    @Cacheable(value = "student")
    public List<StudentDTO> findAll() {
        List<Student> students = (List<Student>) this.studentRepository.findAll();
        return students.stream().map(StudentMapper.INSTANCE::toStudentDTO).toList();
    }

    /**
     * Deletes a student by ID.
     *
     * @param id The UUID of the student to delete.
     * @return A confirmation message.
     * @throws UserNotFoundException if the user is not found.
     */
    @CacheEvict(value = "student", allEntries = true)
    @Transactional
    public String deleteById(UUID id) throws UserNotFoundException {
        Student student = this.studentRepository.findById(id)
                .orElseThrow(StudentNotFoundException::new);
        this.studentRepository.delete(student);
        this.userService.deleteById(student.getUser().getId());
        return "Student with ID: " + id + " deleted successfully.";
    }

    /**
     * Updates a student's information with the provided {@link UpdateStudentDTO} object.
     *
     * @param id               The UUID of the student to update.
     * @param updateStudentDTO Data transfer object containing the updated student's information.
     * @return The updated student as a {@link StudentDTO}.
     * @throws StudentNotFoundException if the student is not found.
     */
    @CacheEvict(value = "student", allEntries = true)
    @Transactional
    public StudentDTO update(UUID id, UpdateStudentDTO updateStudentDTO) throws StudentNotFoundException {
        Student student = this.studentRepository.findById(id)
                .orElseThrow(StudentNotFoundException::new);

        User user = student.getUser();
        user.setName(updateStudentDTO.getName());
        user.setEmail(updateStudentDTO.getEmail());
        user.setGender(updateStudentDTO.getGender());

        return StudentMapper.INSTANCE.toStudentDTO(studentRepository.save(student));
    }


}
