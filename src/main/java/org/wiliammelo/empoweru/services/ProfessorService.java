package org.wiliammelo.empoweru.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.dtos.professor.CreateProfessorDTO;
import org.wiliammelo.empoweru.dtos.professor.UpdateProfessorDTO;
import org.wiliammelo.empoweru.dtos.student.UpdateStudentDTO;
import org.wiliammelo.empoweru.exceptions.UserAlreadyExistsException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.models.Professor;
import org.wiliammelo.empoweru.models.Student;
import org.wiliammelo.empoweru.models.User;
import org.wiliammelo.empoweru.repositories.ProfessorRepository;

import java.util.List;
import java.util.UUID;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Professor create(CreateProfessorDTO professorDTO) throws UserAlreadyExistsException {
        User user = this.userService.create(professorDTO.toUser());
        Professor professor = professorDTO.toProfessor();
        professor.setUser(user);
        return this.professorRepository.save(professor);
    }

    public Professor findById(UUID id) throws UserNotFoundException {
        return this.professorRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }


    public List<Professor> findAll(){
        return (List<Professor>) this.professorRepository.findAll();
    }

    @Transactional
    public String deleteById(UUID id) throws UserNotFoundException {
        Professor professor = this.findById(id);
        this.professorRepository.delete(professor);
        this.userService.deleteById(professor.getUser().getId());
        return "Professor with ID: " + id + " deleted successfully.";
    }

    @Transactional
    public Professor update(UUID id,  UpdateProfessorDTO professorDTO) throws UserNotFoundException{
        Professor professor = findById(id);

        // Atualiza os dados do usuário associado ao estudante
        professor.getUser().setName(professorDTO.getName());
        professor.getUser().setEmail(professorDTO.getEmail());
        professor.getUser().setGender(professorDTO.getGender());
        professor.setBio(professorDTO.getBio());
        professor.setImageUrl(professorDTO.getImageUrl());

        return professorRepository.save(professor);
    }

}
