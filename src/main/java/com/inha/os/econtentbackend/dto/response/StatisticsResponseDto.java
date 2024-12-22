package com.inha.os.econtentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticsResponseDto {
    private Long totalUsers;
    private Long totalMajors;
    private Long totalBooks;
    private Long totalELetters;
    private Long totalContentManagers;
    private Long totalContents;
    private Long totalArticleCount;
}
