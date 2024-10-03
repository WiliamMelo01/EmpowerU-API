package org.wiliammelo.empoweru.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wiliammelo.empoweru.configuration.security.JWTService;
import org.wiliammelo.empoweru.dtos.LoginDTO;
import org.wiliammelo.empoweru.dtos.TokenResponse;
import org.wiliammelo.empoweru.dtos.professor.CreateProfessorDTO;
import org.wiliammelo.empoweru.dtos.professor.ProfessorDTO;
import org.wiliammelo.empoweru.dtos.student.CreateStudentDTO;
import org.wiliammelo.empoweru.dtos.student.StudentDTO;
import org.wiliammelo.empoweru.exceptions.CustomException;
import org.wiliammelo.empoweru.exceptions.UserAlreadyExistsException;
import org.wiliammelo.empoweru.models.User;
import org.wiliammelo.empoweru.repositories.UserRepository;
import org.wiliammelo.empoweru.services.AuthService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JWTService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/public/register/student")
    public ResponseEntity<StudentDTO> registerStudent(@Valid @RequestBody CreateStudentDTO createStudentDTO) throws UserAlreadyExistsException {
        return new ResponseEntity<>(this.authService.registerStudent(createStudentDTO), HttpStatus.CREATED);
    }

    @PostMapping("/public/register/professor")
    public ResponseEntity<ProfessorDTO> registerProfessor(@Valid @RequestBody CreateProfessorDTO createProfessorDTO) throws UserAlreadyExistsException {
        return new ResponseEntity<>(this.authService.registerProfessor(createProfessorDTO), HttpStatus.CREATED);
    }

    @PostMapping("/public/login")
    public ResponseEntity<TokenResponse> loginProfessor(@Valid @RequestBody LoginDTO loginDTO) throws CustomException {
        TokenResponse tokenResponse = authService.login(loginDTO);
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @PostMapping("/public/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestHeader("Authorization") String authorization) throws CustomException {

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new CustomException("Invalid token", HttpStatus.UNAUTHORIZED.value());
        }

        String refreshToken = authorization.substring(7);

        String email = jwtService.validateRefreshToken(refreshToken);

        if (email == null) {
            throw new CustomException("Invalid token", HttpStatus.UNAUTHORIZED.value());
        }

        User user = (User) userRepository.findByEmail(email);

        TokenResponse newTokenResponse = new TokenResponse(jwtService.generateAccessToken(user), refreshToken, user.getRole().getRole());

        return ResponseEntity.ok(newTokenResponse);
    }


}
