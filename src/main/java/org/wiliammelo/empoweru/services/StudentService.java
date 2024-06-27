package org.wiliammelo.empoweru.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.dtos.student.CreateStudentDTO;
import org.wiliammelo.empoweru.dtos.student.UpdateStudentDTO;
import org.wiliammelo.empoweru.exceptions.UserAlreadyExistsException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.models.Student;
import org.wiliammelo.empoweru.models.User;
import org.wiliammelo.empoweru.repositories.StudentRepository;

import java.util.List;
import java.util.UUID;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Student create(CreateStudentDTO createStudentDTO) throws UserAlreadyExistsException {
        User user = this.userService.create(createStudentDTO.toUser());
        Student student = createStudentDTO.toStudent();
        student.setUser(user);
        return this.studentRepository.save(student);
    }

    public Student findById(UUID id)throws UserNotFoundException{
        return this.studentRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public List<Student> findAll(){
        return (List<Student>) this.studentRepository.findAll();
    }

    @Transactional
    public String deleteById(UUID id) throws UserNotFoundException {
        Student student = this.findById(id);
        this.studentRepository.delete(student);
        this.userService.deleteById(student.getUser().getId());
        return "Student with ID: " + id + " deleted successfully.";
    }

    @Transactional
    public Student update(UUID id,  UpdateStudentDTO updateStudentDTO) throws UserNotFoundException{
        Student student = findById(id);

        // Atualiza os dados do usuário associado ao estudante
        student.getUser().setName(updateStudentDTO.getName());
        student.getUser().setEmail(updateStudentDTO.getEmail());
        student.getUser().setGender(updateStudentDTO.getGender());

        return studentRepository.save(student);
    }


}
