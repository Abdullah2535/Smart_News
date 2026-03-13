package com.smartnews.backend.repositories;

import com.smartnews.backend.entities.Sentiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface SentimentRepository extends JpaRepository<Sentiment, Integer> {
}