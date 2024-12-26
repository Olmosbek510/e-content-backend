package com.inha.os.econtentbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleCreateRequestDto {
    private Integer articleId;
    private Integer subjectId;
    private String author;
    private String title;
    private String reference;
    private String path;
    private Integer publishYear;
    @ToString.Exclude
    private MultipartFile file;
}
