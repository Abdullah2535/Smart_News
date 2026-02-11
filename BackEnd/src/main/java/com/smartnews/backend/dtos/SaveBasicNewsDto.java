package com.smartnews.backend.dtos;

import lombok.Data;

@Data
public class SaveBasicNewsDto {
    private int id;
    private String headline;
    private String articleUrl;
    private String source;
    private int categoryId;
    private String thumbnail;
}
