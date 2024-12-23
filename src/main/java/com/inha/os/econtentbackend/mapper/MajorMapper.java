package com.inha.os.econtentbackend.mapper;

import com.inha.os.econtentbackend.dto.response.MajorNameResponseDto;
import com.inha.os.econtentbackend.dto.response.MajorResponseDto;
import com.inha.os.econtentbackend.entity.Major;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MajorMapper {
    @Mapping(source = "id", target = "majorId")
    @Mapping(source = "name", target = "name")
    MajorResponseDto toMajorResponseDTO(Major major);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "id", target = "majorId")
    MajorNameResponseDto toMajorNameResponseDto(Major major);
}
