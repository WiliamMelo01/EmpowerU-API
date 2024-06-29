package org.wiliammelo.empoweru.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.dtos.professor.CreateProfessorDTO;
import org.wiliammelo.empoweru.dtos.professor.ProfessorDTO;
import org.wiliammelo.empoweru.dtos.professor.UpdateProfessorDTO;
import org.wiliammelo.empoweru.exceptions.UserAlreadyExistsException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.mappers.ProfessorMapper;
import org.wiliammelo.empoweru.models.Professor;
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
    public ProfessorDTO create(CreateProfessorDTO professorDTO) throws UserAlreadyExistsException {
        User user = this.userService.create(ProfessorMapper.INSTANCE.toUser(professorDTO));

        Professor professor = ProfessorMapper.INSTANCE.toProfessor(professorDTO);
        professor.setUser(user);
        return  ProfessorMapper.INSTANCE.toProfessorDTO(this.professorRepository.save(professor));
    }

    public ProfessorDTO findById(UUID id) throws UserNotFoundException {
        Professor professor = this.professorRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return ProfessorMapper.INSTANCE.toProfessorDTO(professor);
    }

    public List<ProfessorDTO> findAll(){
        List<Professor> professors = (List<Professor>) professorRepository.findAll();
        return professors.stream().map(ProfessorMapper.INSTANCE::toProfessorDTO).toList();
    }

    @Transactional
    public String deleteById(UUID id) throws UserNotFoundException {
        Professor professor = this.professorRepository.findById(id).orElseThrow(UserNotFoundException::new);
        this.professorRepository.delete(professor);
        this.userService.deleteById(professor.getId());
        return "Professor with ID: " + id + " deleted successfully.";
    }

    @Transactional
    public ProfessorDTO update(UUID id,  UpdateProfessorDTO professorDTO) throws UserNotFoundException{
        Professor professor = this.professorRepository.findById(id).orElseThrow(UserNotFoundException::new);

        // Atualiza os dados do professor
        professor.getUser().setName(professorDTO.getName());
        professor.getUser().setEmail(professorDTO.getEmail());
        professor.getUser().setGender(professorDTO.getGender());
        professor.setBio(professorDTO.getBio());
        professor.setImageUrl(professorDTO.getImageUrl());

        return ProfessorMapper.INSTANCE.toProfessorDTO(professorRepository.save(professor));
    }

}
