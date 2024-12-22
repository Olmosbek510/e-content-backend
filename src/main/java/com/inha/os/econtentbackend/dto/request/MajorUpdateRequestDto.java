package com.inha.os.econtentbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MajorUpdateRequestDto {
    private Integer majorId;
    private String name;
    private String description;
    private String photoName;
    private String photoContent;
}
