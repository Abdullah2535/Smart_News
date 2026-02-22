package com.smartnews.backend.dtos;

import lombok.Data;

@Data
public class SaveBasicNewsDto {
    private Integer id;
    private String headline;
    private String articleUrl;
    private String source;
    private Integer categoryId;
    private String thumbnail;
}
