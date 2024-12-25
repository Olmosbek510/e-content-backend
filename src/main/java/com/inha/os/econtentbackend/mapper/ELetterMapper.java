package com.inha.os.econtentbackend.mapper;

import com.inha.os.econtentbackend.dto.request.ELetterCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.ELetterGetResponseDto;
import com.inha.os.econtentbackend.entity.ELetter;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ELetterMapper {

    @Mapping(source = "author", target = "author")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "relatedTo", target = "relatedTo")
    @Mapping(source = "type", target = "type")
    ELetter toEntity(ELetterCreateRequestDto eLetterCreateRequestDto);

    @Mapping(source = "id", target = "eLetterId")
    ELetterGetResponseDto toGetResponseDto(ELetter eLetter);
}
