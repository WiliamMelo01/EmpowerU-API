package org.wiliammelo.empoweru.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.dtos.evaluation_activity.CreateEvaluationActivityDTO;
import org.wiliammelo.empoweru.exceptions.EvaluationAlreadyExistsException;
import org.wiliammelo.empoweru.exceptions.ProfessorNotFoundException;
import org.wiliammelo.empoweru.exceptions.SectionNotFoundException;
import org.wiliammelo.empoweru.exceptions.UnauthorizedException;
import org.wiliammelo.empoweru.mappers.EvaluationActivityMapper;
import org.wiliammelo.empoweru.models.EvaluationActivity;
import org.wiliammelo.empoweru.models.Professor;
import org.wiliammelo.empoweru.models.Section;
import org.wiliammelo.empoweru.repositories.*;

import java.util.UUID;

/**
 * Service class for handling evaluation activity operations.
 */
@Service
@AllArgsConstructor
public class EvaluationActivityService {

    private final ProfessorRepository professorRepository;
    private final EvaluationActivityRepository evaluationActivityRepository;
    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private QuestionRepository questionRepository;
    private QuestionOptionRepository questionOptionRepository;

    /**
     * Creates a new evaluation activity for a specific section.
     *
     * @param createEvaluationActivityDTO The data transfer object containing the details of the evaluation activity to be created.
     * @param requesterId                 The UUID of the user requesting the creation of the evaluation activity.
     * @return The saved {@link EvaluationActivity} object.
     * @throws ProfessorNotFoundException       If the professor (requester) is not found.
     * @throws SectionNotFoundException         If the section for which the evaluation activity is to be created is not found.
     * @throws UnauthorizedException            If the requester is not the owner of the course to which the section belongs.
     * @throws EvaluationAlreadyExistsException If the section already has an evaluation activity associated with it.
     */
    @Transactional
    public EvaluationActivity create(CreateEvaluationActivityDTO createEvaluationActivityDTO, UUID requesterId) throws ProfessorNotFoundException, SectionNotFoundException, UnauthorizedException, EvaluationAlreadyExistsException {
        Section section = sectionRepository.findById(UUID.fromString(createEvaluationActivityDTO.getSectionId()))
                .orElseThrow(SectionNotFoundException::new);

        if (!isTheOwner(requesterId, section.getCourse().getId())) {
            throw new UnauthorizedException("You are not the owner of the course");
        }

        if (section.getEvaluationActivity() != null) {
            throw new EvaluationAlreadyExistsException();
        }


        EvaluationActivity evaluationActivity = EvaluationActivityMapper.INSTANCE.toEvaluationActivity(createEvaluationActivityDTO);
        section.setEvaluationActivity(evaluationActivity);

        return evaluationActivityRepository.save(evaluationActivity);
    }

    /**
     * Checks if the specified requester is the owner of the course.
     *
     * @param requesterId The UUID of the requester.
     * @param courseId    The UUID of the course to check ownership for.
     * @return true if the requester is the owner of the course, false otherwise.
     */
    private boolean isTheOwner(UUID requesterId, UUID courseId) throws ProfessorNotFoundException {
        Professor professor = professorRepository.findByUserId(requesterId)
                .orElseThrow(ProfessorNotFoundException::new);
        return courseRepository.isTheOwner(courseId, professor.getId());
    }

}
