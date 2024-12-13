package com.inha.os.econtentbackend.dto.response;

import com.inha.os.econtentbackend.entity.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseResponseDto {
    private String status;
    private String roleName;
}
