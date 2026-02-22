package com.smartnews.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserNews {
    private String headline;
    private String articleUrl;
    private String source;
    private String thumbnail;
    private String publishedAt;
    private Integer sentimentId;
}
