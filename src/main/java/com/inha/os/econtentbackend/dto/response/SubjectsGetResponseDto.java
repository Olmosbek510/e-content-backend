package com.inha.os.econtentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SubjectsGetResponseDto {
    private Integer majorId;
    private String majorName;
    private List<SubjectResponseDto> subjects;
}
