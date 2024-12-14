package com.inha.os.econtentbackend.dto.response;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StudentProfileDto extends BaseResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String dateOfBirth;
    private String university;
    private String studentId;
    private String phoneNumber;
    private String address;
    private String work;
    private String photo;
}
