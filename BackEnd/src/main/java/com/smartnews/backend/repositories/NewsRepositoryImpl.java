package com.smartnews.backend.repositories;

import com.smartnews.backend.dtos.UpdateSentimentDto;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


@AllArgsConstructor
public class NewsRepositoryImpl implements CustomNewsUpdateRepository{
    private final JdbcTemplate jdbcTemplate;
    @Override
    @Transactional
    public void batchUpdateStock(List<UpdateSentimentDto> updates) {
        String sql = "UPDATE news SET sentiment_id = ? WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                UpdateSentimentDto dto = updates.get(i);
                ps.setInt(1, dto.getSentimentId());
                ps.setInt(2, dto.getId());
            }
            @Override
            public int getBatchSize() {
                return updates.size();
            }
        });
    }
}