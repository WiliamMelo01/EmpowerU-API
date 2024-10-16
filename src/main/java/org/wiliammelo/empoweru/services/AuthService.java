package org.wiliammelo.empoweru.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.clients.MessagePublisher;
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
@RequiredArgsConstructor
public class AuthService {

    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Qualifier("greetingsSQSPublisher")
    private final MessagePublisher welcomeMessagePublisher;

    @Value("${spring.profiles.active}")
    private String profile;

    @Transactional
    @CacheEvict(value = "student", allEntries = true)
    public StudentDTO registerStudent(CreateStudentDTO createStudentDTO) throws UserAlreadyExistsException {
        this.verifyConflict(createStudentDTO.getEmail());

        Student student = StudentMapper.INSTANCE.toStudent(createStudentDTO);
        student.setPassword(bCryptPasswordEncoder.encode(student.getPassword()));
        student.setRole(UserRole.STUDENT);

        welcomeMessagePublisher.publish(student.getEmail());

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

        welcomeMessagePublisher.publish(professor.getEmail());

        return ProfessorMapper.INSTANCE.toProfessorDTO(this.professorRepository.save(professor));
    }

    public TokenResponse login(LoginDTO loginDTO, HttpServletResponse response) throws CustomException {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        String accessToken = jwtService.generateAccessToken((User) auth.getPrincipal());
        response.addCookie(createRefreshTokenCookie((User) auth.getPrincipal()));

        return new TokenResponse(accessToken,
                getRole(auth)
        );

    }

    public TokenResponse refreshToken(String refreshToken, HttpServletResponse response) throws CustomException {

        if (refreshToken == null) {
            throw new CustomException("Invalid token", HttpStatus.UNAUTHORIZED.value());
        }

        String email = jwtService.validateRefreshToken(refreshToken);

        if (email == null) {
            throw new CustomException("Invalid token", HttpStatus.UNAUTHORIZED.value());
        }

        User user = (User) userRepository.findByEmail(email);

        TokenResponse newTokenResponse = new TokenResponse(jwtService.generateAccessToken(user), user.getRole().getRole());

        response.addCookie(createRefreshTokenCookie(user));

        return newTokenResponse;
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

    private Cookie createRefreshTokenCookie(User user) throws CustomException {
        String refreshToken = jwtService.generateRefreshToken(user);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60);
        refreshTokenCookie.setPath("/");
        if (profile.equals("prod")) {
            refreshTokenCookie.setSecure(true);
        }
        return refreshTokenCookie;
    }

}
