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
public class MajorsGetResponseDto extends BaseResponseDto {
    private List<MajorResponseDto> majors;
}
