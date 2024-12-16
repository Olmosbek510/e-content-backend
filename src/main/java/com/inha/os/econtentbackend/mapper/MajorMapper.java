package com.inha.os.econtentbackend.mapper;

import com.inha.os.econtentbackend.dto.response.MajorResponseDto;
import com.inha.os.econtentbackend.entity.Major;
import com.inha.os.econtentbackend.entity.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface MajorMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "photo", target = "photo", qualifiedByName = "photoToString")
    MajorResponseDto toMajorResponseDTO(Major major);

    @Named("photoToString")
    default String map(Photo value) {
        if (value == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(value.getContent());
    }
}
