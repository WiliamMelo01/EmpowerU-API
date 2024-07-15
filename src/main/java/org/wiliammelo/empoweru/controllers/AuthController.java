package org.wiliammelo.empoweru.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wiliammelo.empoweru.dtos.LoginDTO;
import org.wiliammelo.empoweru.dtos.TokenResponse;
import org.wiliammelo.empoweru.dtos.professor.CreateProfessorDTO;
import org.wiliammelo.empoweru.dtos.professor.ProfessorDTO;
import org.wiliammelo.empoweru.dtos.student.CreateStudentDTO;
import org.wiliammelo.empoweru.dtos.student.StudentDTO;
import org.wiliammelo.empoweru.exceptions.CustomException;
import org.wiliammelo.empoweru.exceptions.UserAlreadyExistsException;
import org.wiliammelo.empoweru.services.AuthService;
import org.wiliammelo.empoweru.services.JWTService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JWTService jwtService;

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
        String token = authService.login(loginDTO);
        return new ResponseEntity<>(new TokenResponse(token, jwtService.getRole(token)), HttpStatus.OK);
    }


}
