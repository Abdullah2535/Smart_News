package com.smartnews.backend.dtos;


/**
 * DTO for {@link com.smartnews.backend.entities.News}
 */
import lombok.Data;

@Data
public class SearchResultDto {
    private Integer id;
    private String headline;
    private Integer sentimentId;
    private String thumbnail;
    private String articleUrl;
    private Float credibilityScore;
    private double similarityScore; // The math result (e.g., 0.85)
    private String credibilityLabel;


}