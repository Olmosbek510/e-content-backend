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
public class ArticleCreateRequestDto {
    private Integer articleId;
    private Integer subjectId;
    private String title;
    private String reference;
    private Integer publishYear;
    @JsonIgnore
    private MultipartFile file;
}
