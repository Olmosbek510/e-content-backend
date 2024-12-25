package com.inha.os.econtentbackend.dto.request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleResponseDto {
    private Integer articleId;
    private String title;
    private String reference;
    @Column(nullable = false)
    private Integer publishYear;
}
