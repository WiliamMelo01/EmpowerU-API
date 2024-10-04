package org.wiliammelo.empoweru.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wiliammelo.empoweru.dtos.CustomResponse;
import org.wiliammelo.empoweru.exceptions.CanNotIssueCertificateException;
import org.wiliammelo.empoweru.exceptions.CourseNotFoundException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.models.User;
import org.wiliammelo.empoweru.services.CertificateService;

import java.util.UUID;

@RestController
@RequestMapping("/certificates")
@AllArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping("/issue/{courseId}")
    public ResponseEntity<CustomResponse> issueCertificate(@PathVariable("courseId") UUID courseId, @AuthenticationPrincipal User user) throws CanNotIssueCertificateException, CourseNotFoundException, UserNotFoundException {
        return new ResponseEntity<>(new CustomResponse(certificateService.issue(courseId, user.getId()), HttpStatus.OK.value()), HttpStatus.OK);
    }

}
