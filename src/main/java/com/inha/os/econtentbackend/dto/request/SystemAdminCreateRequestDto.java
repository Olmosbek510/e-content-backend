package com.inha.os.econtentbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemAdminCreateRequestDto {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
