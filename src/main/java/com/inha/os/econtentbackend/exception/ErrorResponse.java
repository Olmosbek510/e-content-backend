package com.inha.os.econtentbackend.exception;

import com.inha.os.econtentbackend.dto.response.BaseResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ErrorResponse extends BaseResponseDto {
    private String message;
}
