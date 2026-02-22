package com.smartnews.backend.controllers;

import com.smartnews.backend.dtos.*;
import com.smartnews.backend.entities.Preference;
import com.smartnews.backend.mappers.NewsMapper;
import com.smartnews.backend.repositories.NewsRepository;
import com.smartnews.backend.repositories.PreferenceRepository;
import com.smartnews.backend.repositories.UserRepository;
import com.smartnews.backend.services.PreferenceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/news")
public class NewsController {
  private final PreferenceRepository preferenceRepository;
  private final UserRepository userRepository;
  private NewsMapper newsMapper;
  private NewsRepository newsRepository;
  private PreferenceService preferenceService;



  @PostMapping("")
    public ResponseEntity<Void> saveFromCrawler(@RequestBody List<SaveBasicNewsDto> news){
      for (SaveBasicNewsDto article : news){
          newsRepository.save(newsMapper.toEntity(article));
      }
      return ResponseEntity.ok().build();
  }
  @GetMapping("/ai")
    public ResponseEntity<List<NewsForAiDto>> getBasicNews(){
      //var news = newsRepository.findBySentimentIsNull();
     // System.out.println(news);
      return  ResponseEntity.ok().body(newsRepository.findBySentimentIsNull());
  }
  @PutMapping("/ai")
    public ResponseEntity<String>updateSentiment(@RequestBody List<UpdateSentimentDto> updates){
    if (updates.isEmpty()) {
      return ResponseEntity.badRequest().body("No data provided");
    }
    newsRepository.batchUpdateStock(updates);
    return ResponseEntity.ok("Successfully updated " + updates.size() + " items.");
  }
  @PostMapping("userP")
  public ResponseEntity<List<UserNews>> getUserNews(@RequestBody UserPreference choices){
    if (choices.getUserId() == null ||  choices.getCategoryIds() == null || choices.getSentimentIds() == null ){
      return ResponseEntity.badRequest().body(null);
    }
     var user = userRepository.findUserById(choices.getUserId());
     var newChoices =preferenceService.changePreferenceContent(choices);
      Preference preference;
      if (user.getPreference() == null){
          preference = new Preference(newChoices, user);
      }else{
        preference = preferenceRepository.findById(user.getPreference().getId()).orElse(null);
        assert preference != null;
        preference.setPreferenceContent(newChoices);
      }
      preferenceRepository.save(preference);
      var news = newsRepository.findBySentimentAndCategory(choices.getCategoryIds(), choices.getSentimentIds());
     return ResponseEntity.ok().body(news);
  }


}
