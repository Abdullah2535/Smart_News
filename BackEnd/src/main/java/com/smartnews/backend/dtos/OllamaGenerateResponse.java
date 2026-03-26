package com.smartnews.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

// This stops Spring Boot from crashing when Ollama sends extra data
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OllamaGenerateResponse {
    // This perfectly matches the "response" key in Ollama's JSON
    private String response;

}
