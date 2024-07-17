package org.wiliammelo.empoweru.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.dtos.evaluation_activity_result.CreateEvaluationActivityResultDTO;
import org.wiliammelo.empoweru.exceptions.CourseNotFoundException;
import org.wiliammelo.empoweru.exceptions.EvaluationActivityNotFoundException;
import org.wiliammelo.empoweru.exceptions.StudentNotFoundException;
import org.wiliammelo.empoweru.mappers.EvaluationActivityResultMapper;
import org.wiliammelo.empoweru.models.EvaluationActivityResult;
import org.wiliammelo.empoweru.repositories.CourseRepository;
import org.wiliammelo.empoweru.repositories.EvaluationActivityRepository;
import org.wiliammelo.empoweru.repositories.EvaluationActivityResultRepository;
import org.wiliammelo.empoweru.repositories.StudentRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EvaluationActivityResultService {

    private final EvaluationActivityResultRepository evaluationActivityResultRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EvaluationActivityRepository evaluationRepository;

    /**
     * Creates a new evaluation activity result based on the provided DTO.
     * This method checks for the existence of the student, course, and evaluation activity
     * before proceeding to create or update an evaluation activity result.
     * If the student, course, or evaluation activity does not exist, it throws the respective not found exception.
     * If an evaluation activity result for the given student and evaluation activity already exists,
     * it updates the grade if the new grade is higher than the existing one.
     *
     * @param createEvaluationActivityResultDTO The DTO containing the details for creating or updating an evaluation activity result.
     * @return The newly created or updated EvaluationActivityResult.
     * @throws CourseNotFoundException             If the course associated with the DTO does not exist.
     * @throws StudentNotFoundException            If the student associated with the DTO does not exist.
     * @throws EvaluationActivityNotFoundException If the evaluation activity associated with the DTO does not exist.
     */
    public EvaluationActivityResult create(CreateEvaluationActivityResultDTO createEvaluationActivityResultDTO) throws CourseNotFoundException, StudentNotFoundException, EvaluationActivityNotFoundException {
        UUID studentId = UUID.fromString(createEvaluationActivityResultDTO.getStudentId());
        UUID courseId = UUID.fromString(createEvaluationActivityResultDTO.getCourseId());
        UUID evaluationId = UUID.fromString(createEvaluationActivityResultDTO.getEvaluationId());

        boolean studentExists = studentRepository.existsById(studentId);

        if (!studentExists) {
            throw new StudentNotFoundException();
        }

        boolean courseExists = courseRepository.existsById(courseId);

        if (!courseExists) {
            throw new CourseNotFoundException();
        }

        boolean evaluationActivityExists = evaluationRepository.existsById(evaluationId);

        if (!evaluationActivityExists) {
            throw new EvaluationActivityNotFoundException();
        }

        Optional<EvaluationActivityResult> evaluationActivityResult1 = evaluationActivityResultRepository.findByEvaluationIdAndStudentId(evaluationId, studentId);

        if (evaluationActivityResult1.isEmpty()) {
            EvaluationActivityResult evaluationActivityResult = EvaluationActivityResultMapper.INSTANCE.toEvaluationActivityResult(createEvaluationActivityResultDTO);
            return evaluationActivityResultRepository.save(evaluationActivityResult);
        }

        if (createEvaluationActivityResultDTO.getGrade() > evaluationActivityResult1.get().getGrade()) {
            evaluationActivityResult1.get().setGrade(createEvaluationActivityResultDTO.getGrade());
            return evaluationActivityResultRepository.save(evaluationActivityResult1.get());
        }

        return evaluationActivityResult1.get();
    }

}
