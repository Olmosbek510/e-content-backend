package com.inha.os.econtentbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentManagerCreateRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String base64Photo;
    private String photoName;
}
