package com.smartnews.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UpdateSentimentDto {
    // Python ONLY sends this
    private Integer id;

    // Java fills these in internally
    @JsonIgnore
    private Integer sentimentId;

    @JsonIgnore
    private double[] embeddingVector;
    @JsonIgnore
    private Float credibilityScore;
}
