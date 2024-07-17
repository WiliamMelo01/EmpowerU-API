package org.wiliammelo.empoweru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.wiliammelo.empoweru.dtos.evaluation_activity.CreateEvaluationActivityDTO;
import org.wiliammelo.empoweru.dtos.evaluation_activity.CreateQuestionOptionDTO;
import org.wiliammelo.empoweru.models.EvaluationActivity;
import org.wiliammelo.empoweru.models.QuestionOption;

@Mapper
public interface EvaluationActivityMapper {

    EvaluationActivityMapper INSTANCE = Mappers.getMapper(EvaluationActivityMapper.class);

    @Mapping(target = "id", ignore = true)
    EvaluationActivity toEvaluationActivity(CreateEvaluationActivityDTO createEvaluationActivityDTO);

    @Mapping(target = "correct", source = "correct")
    QuestionOption toQuestionOption(CreateQuestionOptionDTO createQuestionOptionDTO);

}
