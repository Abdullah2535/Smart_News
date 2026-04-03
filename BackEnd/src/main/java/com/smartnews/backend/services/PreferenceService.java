package com.smartnews.backend.services;

import com.smartnews.backend.dtos.IdValuePair;
import com.smartnews.backend.dtos.PreferencesResponse;
import com.smartnews.backend.dtos.UserPreference;
import com.smartnews.backend.repositories.CategoryRepository;
import com.smartnews.backend.repositories.SentimentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;




@Service
@AllArgsConstructor
public class PreferenceService {
    private CategoryRepository categoryRepository;
    private SentimentRepository sentimentRepository ;

    public String changePreferenceContent(UserPreference userPreference) {
        StringBuilder preferenceContent = new StringBuilder();
        preferenceContent.append("C,");
        for (Integer categoryId : userPreference.getCategoryIds()) {
            preferenceContent.append(categoryId).append(",");
        }
        preferenceContent.append("S,");
        for (Integer categoryId : userPreference.getSentimentIds()) {
            preferenceContent.append(categoryId).append(",");
        }
        preferenceContent.deleteCharAt(preferenceContent.length() - 1);
        return preferenceContent.toString();
    }
    public UserPreference getPreferenceIds(String preferenceContent) {
        UserPreference preferenceIds = new UserPreference();
        if (preferenceContent == null || preferenceContent.isEmpty()) {
            return preferenceIds;
        }
        char type = preferenceContent.charAt(0);
        for (String preferenceId : preferenceContent.split(",")) {
           if (preferenceId.charAt(preferenceId.length() - 1) == 'S') {
               type = 'S';
           }
            if (type == 'C') {
                preferenceIds.getCategoryIds().add(Integer.parseInt(preferenceId));
            }
            else
                preferenceIds.getSentimentIds().add(Integer.parseInt(preferenceId));
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
