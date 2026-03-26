package com.smartnews.backend.services;

import com.smartnews.backend.dtos.IdValuePair;
import com.smartnews.backend.dtos.PreferencesResponse;
import com.smartnews.backend.dtos.UserPreference;
import com.smartnews.backend.repositories.CategoryRepository;
import com.smartnews.backend.repositories.SentimentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
@AllArgsConstructor
public class PreferenceService {
    private CategoryRepository categoryRepository;
    private SentimentRepository sentimentRepository ;

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
    public PreferencesResponse getPreferences(){
        PreferencesResponse preferencesResponse = new PreferencesResponse();
        preferencesResponse.setCategories(
                categoryRepository.findAll()
                        .stream()
                        .map(c -> new IdValuePair(c.getId(), c.getName()))
                        .toList()
        );
        preferencesResponse.setSentiments(sentimentRepository.findAll().stream().map(
                s->new IdValuePair(s.getId(),s.getType())).toList());
        return preferencesResponse;
    }

}
