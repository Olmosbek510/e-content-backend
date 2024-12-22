package com.inha.os.econtentbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentCreateDto {
    @NotBlank(message = "first name cannot be blank")
    private String firstName;
    @NotBlank(message = "last name cannot be blank")
    private String lastName;
    @NotBlank(message = "email cannot be blank")
    private String email;
    @NotBlank(message = "password cannot be blank")
    private String password;
    @NotBlank(message = "studentId cannot be blank")
    private String studentId;
    @NotBlank(message = "university cannot bu blank")
    private String university;
    @NotBlank(message = "birth date cannot be blank")
    private String birthDate;
    @NotBlank(message = "phone number cannot be blank")
    private String phoneNumber;
}
