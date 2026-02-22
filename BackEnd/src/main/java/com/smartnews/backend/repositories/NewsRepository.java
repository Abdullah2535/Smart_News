package com.smartnews.backend.repositories;

import com.smartnews.backend.dtos.NewsForAiDto;
import com.smartnews.backend.dtos.UserNews;
import com.smartnews.backend.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface NewsRepository extends JpaRepository<News, Integer>,CustomNewsUpdateRepository {
    @Query(value = "SELECT n.id,n.headline FROM news n where n.sentiment_id IS NULL ",nativeQuery = true)
    List<NewsForAiDto> findBySentimentIsNull( );

@Query(
        value = "SELECT n.headline,n.article_url,n.source,n.thumbnail,DATE_FORMAT(n.published_at, '%Y-%m-%dT%H:%i:%sZ') as event_time,sentiment_id " +
                "FROM news n WHERE category_id IN :categoryIds AND sentiment_id IN :sentimentIds",
        nativeQuery = true
)
List<UserNews> findBySentimentAndCategory(@Param("categoryIds") Set<Integer> categoryIds,
                                          @Param("sentimentIds") Set<Integer> sentimentIds);
}