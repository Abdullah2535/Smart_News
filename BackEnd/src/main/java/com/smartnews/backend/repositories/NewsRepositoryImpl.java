package com.smartnews.backend.repositories;

import com.smartnews.backend.dtos.UpdateSentimentDto;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
@AllArgsConstructor
public class NewsRepositoryImpl implements CustomNewsUpdateRepository{
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    @Override
    @Transactional
    public void batchUpdateStock(List<UpdateSentimentDto> updates) {
        // 1. ADDED: credibility_score = ?
        String sql = "UPDATE news SET sentiment_id = ?, credibility_score = ?, embedding_array = CAST(? AS JSON) WHERE id = ?";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                UpdateSentimentDto dto = updates.get(i);

                // Set Parameter 1: The sentiment ID
                ps.setInt(1, dto.getSentimentId());

                // Set Parameter 2: The NEW Credibility Score
                if (dto.getCredibilityScore() != null) {
                    ps.setFloat(2, dto.getCredibilityScore());
                } else {
                    // Safely save as NULL in the database if Qwen failed
                    ps.setNull(2, java.sql.Types.FLOAT);
                }

                // Set Parameter 3: The Embedding Vector (Shifted from 2 to 3)
                try {
                    String jsonArray = objectMapper.writeValueAsString(dto.getEmbeddingVector());
                    ps.setString(3, jsonArray);
                } catch (Exception e) {
                    throw new SQLException("Failed to convert vector array to JSON string", e);
                }

                // Set Parameter 4: The Article ID (Shifted from 3 to 4)
                ps.setInt(4, dto.getId());
            }

            @Override
            public int getBatchSize() {
                return updates.size();
            }
        });
    }
}