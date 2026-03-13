package com.smartnews.backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PreferencesResponse {
    List <IdValuePair> sentiments;
    List <IdValuePair> categories;
}
