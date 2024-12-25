package com.inha.os.econtentbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ELetterCreateRequestDto {
    private String author;
    private String title;
    private Integer subjectId;
    private String type;
    private String relatedTo;
    @JsonIgnore
    private MultipartFile file;
    private String path;
}
