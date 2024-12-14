package com.inha.os.econtentbackend.mapper;

import com.inha.os.econtentbackend.dto.response.MajorResponseDto;
import com.inha.os.econtentbackend.entity.Major;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MajorMapper {
//    @Mapping(source = "id", target = "id")
//    MajorResponseDto toMajorResponseDTO(Major major);
}
