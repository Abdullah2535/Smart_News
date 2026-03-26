package com.smartnews.backend.controllers;

import com.smartnews.backend.dtos.*;
import com.smartnews.backend.entities.Preference;
import com.smartnews.backend.mappers.NewsMapper;
import com.smartnews.backend.repositories.NewsRepository;
import com.smartnews.backend.repositories.PreferenceRepository;
import com.smartnews.backend.repositories.UserRepository;
import com.smartnews.backend.services.PreferenceService;
import com.smartnews.backend.services.SearchService;
import com.smartnews.backend.services.VectorGenerationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/news")
public class NewsController {
  private final PreferenceRepository preferenceRepository;
  private final UserRepository userRepository;
  private final VectorGenerationService vectorGenerationService;
  private NewsMapper newsMapper;
  private NewsRepository newsRepository;
  private PreferenceService preferenceService;
  private SearchService searchService;



  @PostMapping("")
    public ResponseEntity<Void> saveFromCrawler(@RequestBody List<SaveBasicNewsDto> news){
      for (SaveBasicNewsDto article : news){
          newsRepository.save(newsMapper.toEntity(article));
      }
      return ResponseEntity.ok().build();
  }
  @GetMapping("/ai")
    public ResponseEntity<List<NewsForAiDto>> getBasicNews(){

      return  ResponseEntity.ok().body(newsRepository.findBySentimentIsNull());
  }
    @PutMapping("/ai")
    public ResponseEntity<String> processAiPipeline() {

        // 2. Hand the list of IDs to your orchestrator
        // (This will fetch the text, get Qwen sentiment, get Nomic vectors, and save to DB)
        vectorGenerationService.processPendingArticles();

        // 3. Return success message to Python
        return ResponseEntity.accepted().body("AI Pipeline Triggered! Scanning database for empty articles...");
  }
  @PostMapping("userP")
  public ResponseEntity<List<UserNews>> getUserNews(@RequestBody UserPreference choices,
                                                    Principal principal){
    if (choices.getCategoryIds() == null || choices.getSentimentIds() == null ){
      return ResponseEntity.badRequest().body(null);
    }
      Integer userId = Integer.valueOf(principal.getName());
     var user = userRepository.findUserById(userId);
     var newChoices =preferenceService.changePreferenceContent(choices);
      Preference preference =new Preference(newChoices, user);

      preferenceRepository.save(preference);
      var news = newsRepository.findBySentimentAndCategory(choices.getCategoryIds(), choices.getSentimentIds());
     return ResponseEntity.ok().body(news);
  }


    @GetMapping("/search")
    public ResponseEntity<List<SearchResultDto>> semanticSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "20") int limit) { // Default to 20 if not provided

        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<SearchResultDto> results = searchService.searchNews(query, limit);
        return ResponseEntity.ok(results);
    }


}
