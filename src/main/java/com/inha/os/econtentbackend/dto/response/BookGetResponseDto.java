package com.inha.os.econtentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookGetResponseDto {
    private Integer bookId;
    private String title;
    private String author;
    private Integer pageCount;
    private String description;
}
