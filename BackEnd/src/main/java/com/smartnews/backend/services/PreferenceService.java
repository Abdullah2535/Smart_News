package com.smartnews.backend.services;

import com.smartnews.backend.dtos.UserPreference;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class PreferenceService {
    public String changePreferenceContent(UserPreference userPreference) {
        StringBuilder preferenceContent = new StringBuilder();
        for (Integer categoryId : userPreference.getCategoryIds()) {
            preferenceContent.append(categoryId).append(",");
        }
        for (Integer categoryId : userPreference.getSentimentIds()) {
            preferenceContent.append(categoryId).append(",");
        }
        preferenceContent.deleteCharAt(preferenceContent.length() - 1);
        return preferenceContent.toString();
    }
    public Set<Integer> getPreferenceIds(String preferenceContent) {
        Set<Integer> preferenceIds = new HashSet<>();
        if (preferenceContent == null || preferenceContent.isEmpty()) {
            return preferenceIds;
        }
        for (String preferenceId : preferenceContent.split(",")) {
            preferenceIds.add(Integer.parseInt(preferenceId));
        }
        return preferenceIds;
    }


}
