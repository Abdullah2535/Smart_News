package com.smartnews.backend.dtos;


import lombok.Data;

import java.util.Set;

@Data
public class UserPreference {
    private Set<Integer> categoryIds;
    private Set<Integer> sentimentIds;
}
