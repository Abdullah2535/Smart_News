package com.smartnews.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewsForAiDto {
    private Integer id;
    private String headline;
}
