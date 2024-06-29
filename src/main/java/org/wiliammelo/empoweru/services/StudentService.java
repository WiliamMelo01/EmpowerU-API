package org.wiliammelo.empoweru.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.dtos.student.CreateStudentDTO;
import org.wiliammelo.empoweru.dtos.student.StudentDTO;
import org.wiliammelo.empoweru.dtos.student.UpdateStudentDTO;
import org.wiliammelo.empoweru.exceptions.UserAlreadyExistsException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.mappers.StudentMapper;
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
    public StudentDTO create(CreateStudentDTO createStudentDTO) throws UserAlreadyExistsException {
        User user = this.userService.create(StudentMapper.INSTANCE.toUser(createStudentDTO));
        Student student = new Student();
        student.setUser(user);
        return StudentMapper.INSTANCE.toStudentDTO(this.studentRepository.save(student));
    }

    public StudentDTO findById(UUID id)throws UserNotFoundException{
        Student student = this.studentRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return StudentMapper.INSTANCE.toStudentDTO(student);
    }

    public List<StudentDTO> findAll(){
        List<Student> students =  (List<Student>) this.studentRepository.findAll();
        return students.stream().map(StudentMapper.INSTANCE::toStudentDTO).toList();
    }

    @Transactional
    public String deleteById(UUID id) throws UserNotFoundException {
        Student student = this.studentRepository.findById(id).orElseThrow(UserNotFoundException::new);
        this.studentRepository.delete(student);
        this.userService.deleteById(student.getUser().getId());
        return "Student with ID: " + id + " deleted successfully.";
    }

    @Transactional
    public StudentDTO update(UUID id,  UpdateStudentDTO updateStudentDTO) throws UserNotFoundException{
        Student student = this.studentRepository.findById(id).orElseThrow(UserNotFoundException::new);

        // Atualiza os dados do usuário associado ao estudante
        student.getUser().setName(updateStudentDTO.getName());
        student.getUser().setEmail(updateStudentDTO.getEmail());
        student.getUser().setGender(updateStudentDTO.getGender());

        return StudentMapper.INSTANCE.toStudentDTO(studentRepository.save(student));
    }


}
