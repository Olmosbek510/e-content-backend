package com.inha.os.econtentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StudentCreateResponseDto extends BaseResponseDto {
    private String email;
    private String studentId;
    private String password;
}
