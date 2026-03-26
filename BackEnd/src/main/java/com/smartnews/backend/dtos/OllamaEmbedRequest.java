package com.smartnews.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OllamaEmbedRequest {
    private String model;
    private List<String> input;
}