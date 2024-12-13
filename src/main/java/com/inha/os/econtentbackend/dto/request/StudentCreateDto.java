package com.inha.os.econtentbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    @NotBlank(message = "confirm password cannot be blank")
    private String confirmPassword;
    @NotBlank(message = "studentId cannot be blank")
    private String studentId;
    @NotBlank(message = "university cannot bu blank")
    private String university;
    @NotNull(message = "birth date cannot be null")
    private LocalDate birthDate;
    @NotBlank(message = "username cannot be blank")
    private String username;
    @NotBlank(message = "phone number cannot be blank")
    private String phoneNumber;
    @NotBlank(message = "address cannot be blank")
    private String address;
}
