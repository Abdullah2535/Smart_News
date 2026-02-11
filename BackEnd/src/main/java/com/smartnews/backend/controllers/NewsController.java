package com.smartnews.backend.controllers;

import com.smartnews.backend.dtos.NewsForAiDto;
import com.smartnews.backend.dtos.SaveBasicNewsDto;
import com.smartnews.backend.dtos.UpdateSentimentDto;
import com.smartnews.backend.mappers.NewsMapper;
import com.smartnews.backend.repositories.NewsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/news")
public class NewsController {
    private NewsMapper newsMapper;
    private NewsRepository newsRepository;


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

}
