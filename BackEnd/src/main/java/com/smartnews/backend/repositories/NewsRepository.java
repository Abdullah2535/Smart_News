package com.smartnews.backend.repositories;

import com.smartnews.backend.dtos.NewsForAiDto;
import com.smartnews.backend.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Integer>,CustomNewsUpdateRepository {
    @Query(value = "SELECT n.id,n.headline FROM news n where n.sentiment_id IS NULL ",nativeQuery = true)
    List<NewsForAiDto> findBySentimentIsNull( );
}