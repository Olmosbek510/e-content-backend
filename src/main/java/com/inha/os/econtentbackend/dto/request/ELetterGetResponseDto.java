package com.inha.os.econtentbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ELetterGetResponseDto {
    private Integer eLetterId;
    private String title;
    private String author;
    private String relatedTo;
    private String type;
}
