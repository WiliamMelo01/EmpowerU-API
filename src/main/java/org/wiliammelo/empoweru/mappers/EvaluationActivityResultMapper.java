package org.wiliammelo.empoweru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.wiliammelo.empoweru.dtos.evaluation_activity_result.CreateEvaluationActivityResultDTO;
import org.wiliammelo.empoweru.models.EvaluationActivityResult;

import java.util.UUID;

@Mapper
public interface EvaluationActivityResultMapper {

    EvaluationActivityResultMapper INSTANCE = Mappers.getMapper(EvaluationActivityResultMapper.class);

    @Mapping(target = "id", ignore = true)
    EvaluationActivityResult toEvaluationActivityResult(CreateEvaluationActivityResultDTO createEvaluationActivityResultDTO);

    default UUID map(String value) {
        return UUID.fromString(value);
    }

}
