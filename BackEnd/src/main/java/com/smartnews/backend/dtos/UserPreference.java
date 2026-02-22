package com.smartnews.backend.dtos;


import lombok.Getter;

import java.util.Set;

@Getter
public class UserPreference {
    private Integer userId;
    private Set<Integer> categoryIds;
    private Set<Integer> sentimentIds;
}
