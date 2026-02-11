package com.smartnews.backend.repositories;

import com.smartnews.backend.dtos.UpdateSentimentDto;

import java.util.List;

public interface CustomNewsUpdateRepository {
    void batchUpdateStock(List<UpdateSentimentDto> updates);
}
