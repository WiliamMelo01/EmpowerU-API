package org.wiliammelo.empoweru.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.configuration.security.JWTService;
import org.wiliammelo.empoweru.dtos.LoginDTO;
import org.wiliammelo.empoweru.dtos.TokenResponse;
import org.wiliammelo.empoweru.dtos.professor.CreateProfessorDTO;
import org.wiliammelo.empoweru.dtos.professor.ProfessorDTO;
import org.wiliammelo.empoweru.dtos.student.CreateStudentDTO;
import org.wiliammelo.empoweru.dtos.student.StudentDTO;
import org.wiliammelo.empoweru.exceptions.CustomException;
import org.wiliammelo.empoweru.exceptions.UserAlreadyExistsException;
import org.wiliammelo.empoweru.mappers.ProfessorMapper;
import org.wiliammelo.empoweru.mappers.StudentMapper;
import org.wiliammelo.empoweru.models.Professor;
import org.wiliammelo.empoweru.models.Student;
import org.wiliammelo.empoweru.models.User;
import org.wiliammelo.empoweru.models.UserRole;
import org.wiliammelo.empoweru.repositories.ProfessorRepository;
import org.wiliammelo.empoweru.repositories.StudentRepository;
import org.wiliammelo.empoweru.repositories.UserRepository;

@Service
@AllArgsConstructor
public class AuthService {

    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Transactional
    @CacheEvict(value = "student", allEntries = true)
    public StudentDTO registerStudent(CreateStudentDTO createStudentDTO) throws UserAlreadyExistsException {
        this.verifyConflict(createStudentDTO.getEmail());

        Student student = StudentMapper.INSTANCE.toStudent(createStudentDTO);
        student.setPassword(bCryptPasswordEncoder.encode(student.getPassword()));
        student.setRole(UserRole.STUDENT);

        return StudentMapper.INSTANCE.toStudentDTO(this.studentRepository.save(student));
    }

    @Transactional
    @CacheEvict(value = "professor", allEntries = true)
    public ProfessorDTO registerProfessor(CreateProfessorDTO createProfessorDTO) throws UserAlreadyExistsException {
        this.verifyConflict(createProfessorDTO.getEmail());

        Professor professor = ProfessorMapper.INSTANCE.toProfessor(createProfessorDTO);
        professor.setPassword(bCryptPasswordEncoder.encode(professor.getPassword()));
        professor.setRole(UserRole.PROFESSOR);
        professor.setImageUrl(createProfessorDTO.getImageUrl());
        professor.setBio(createProfessorDTO.getBio());
        return ProfessorMapper.INSTANCE.toProfessorDTO(this.professorRepository.save(professor));
    }

    public TokenResponse login(LoginDTO loginDTO) throws CustomException {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        String accessToken = jwtService.generateAccessToken((User) auth.getPrincipal());
        String refreshToken = jwtService.generateRefreshToken((User) auth.getPrincipal());

        return new TokenResponse(accessToken,
                refreshToken,
                getRole(auth)
        );

    }

    private void verifyConflict(String email) throws UserAlreadyExistsException {
        if (this.userRepository.existsByEmail(email) == Boolean.TRUE) {
            throw new UserAlreadyExistsException("User already exists");
        }
    }

    private String getRole(Authentication auth) {
        return auth.getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority()
                .replace("ROLE_", "");
    }

}
