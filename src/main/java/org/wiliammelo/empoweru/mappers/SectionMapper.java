package org.wiliammelo.empoweru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.wiliammelo.empoweru.dtos.section.CreateSectionDTO;
import org.wiliammelo.empoweru.dtos.section.SectionDTO;
import org.wiliammelo.empoweru.dtos.section.SectionDetailedDTO;
import org.wiliammelo.empoweru.models.Section;

@Mapper
public interface SectionMapper {

    SectionMapper INSTANCE = Mappers.getMapper(SectionMapper.class);

    Section toSection(CreateSectionDTO sectionDTO);

    @Mapping(target = "courseId", source = "course.id")
    SectionDTO toSectionDTO(Section section);

    @Mapping(target = "videos", source = "videos")
    SectionDetailedDTO toSectionDetailedDTO(Section section);
}
