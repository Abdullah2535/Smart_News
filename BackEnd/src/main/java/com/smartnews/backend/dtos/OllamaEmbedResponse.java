package com.smartnews.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OllamaEmbedResponse {

    // The 2D matrix of vectors
    private double[][] embeddings;

}