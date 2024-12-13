package com.inha.os.econtentbackend.dto.response;

import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseResponseDto {
    private ResponseStatus status;
    private String roleName;
}
