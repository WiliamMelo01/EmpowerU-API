package org.wiliammelo.empoweru.services;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.dtos.certificate.IssueCertificateRequestDTO;
import org.wiliammelo.empoweru.exceptions.CanNotIssueCertificateException;
import org.wiliammelo.empoweru.exceptions.CourseNotFoundException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.models.Course;
import org.wiliammelo.empoweru.models.EvaluationActivityResult;
import org.wiliammelo.empoweru.models.Student;
import org.wiliammelo.empoweru.repositories.CourseRepository;
import org.wiliammelo.empoweru.repositories.EvaluationActivityResultRepository;
import org.wiliammelo.empoweru.repositories.StudentRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CertificateService {
    private final EvaluationActivityResultRepository evaluationActivityResultRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final RabbitTemplate rabbitTemplate;

    private static final float MIN_GRADE = 7.0f;

    public String issue(UUID courseId, UUID requesterId) throws CourseNotFoundException, CanNotIssueCertificateException, UserNotFoundException {
        Student student = studentRepository.findByUserId(requesterId)
                .orElseThrow(UserNotFoundException::new);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);

        if (canIssueCertificate(student.getId(), course)) {
            IssueCertificateRequestDTO issueCertificateRequestDTO = IssueCertificateRequestDTO.builder()
                    .userName(student.getUser().getEmail())
                    .courseTitle(course.getTitle())
                    .email(student.getUser().getEmail())
                    .build();
            publishCertificateRequest(issueCertificateRequestDTO);
            return "Issue in progress. As soon as it is ready, you will receive an email.";
        }

        throw new CanNotIssueCertificateException();
    }

    private void publishCertificateRequest(IssueCertificateRequestDTO issueCertificateRequestDTO) {
        Gson gson = new Gson();
        rabbitTemplate.convertAndSend("certificateExchange", "certificateRoutingKey", gson.toJson(issueCertificateRequestDTO));
    }

    private boolean canIssueCertificate(UUID userId, Course course) {
        List<EvaluationActivityResult> evaluationResults = evaluationActivityResultRepository.findAllByCourseIdAndStudentId(course.getId(), userId);

        int activitiesCount = countActivities(course);

        if (activitiesCount == 0) {
            return true;
        }

        return allActivitiesDone(evaluationResults, activitiesCount) && allGradesApproved(evaluationResults);
    }

    private int countActivities(Course course) {
        return course.getSections().stream().reduce(0, (subtotal, section) -> {
            if (section.getEvaluationActivity() != null) {
                return subtotal + 1;
            }
            return subtotal;
        }, Integer::sum);
    }

    private boolean allActivitiesDone(List<EvaluationActivityResult> evaluationResults, int activitiesCount) {
        return activitiesCount == evaluationResults.size();
    }

    private boolean allGradesApproved(List<EvaluationActivityResult> evaluationResults) {
        return evaluationResults.stream()
                .allMatch(evaluationResult -> evaluationResult.getGrade() >= MIN_GRADE);
    }

}
