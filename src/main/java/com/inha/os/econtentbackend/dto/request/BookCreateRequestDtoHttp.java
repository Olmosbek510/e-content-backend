package com.inha.os.econtentbackend.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookCreateRequestDtoHttp {
    @ToString.Exclude
    private MultipartFile book;
    private String title;
    private String author;
    private Integer pageCount;
    private String description;
    private Integer subjectId;
    private String path;
}
