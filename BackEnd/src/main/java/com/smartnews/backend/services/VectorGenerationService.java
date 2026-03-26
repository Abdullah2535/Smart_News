package com.smartnews.backend.services;

import com.smartnews.backend.dtos.NewsForAiDto;
import com.smartnews.backend.dtos.UpdateSentimentDto;
import com.smartnews.backend.repositories.NewsRepository;
import com.smartnews.backend.services.EmbeddingService;
import com.smartnews.backend.services.SentimentService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class VectorGenerationService {

    private final NewsRepository newsRepository;
    private final EmbeddingService embeddingService;
    private final SentimentService sentimentService;

    @Async
    @Transactional
    public void processPendingArticles() {

        long startTime = System.currentTimeMillis();

        // 1. THE SWEEP: Use your highly optimized native query!
        List<NewsForAiDto> pendingArticles = newsRepository.findBySentimentIsNull();

        if (pendingArticles.isEmpty()) {
            System.out.println("✅ AI Sweep Complete: No empty articles found in the database.");
            return;
        }

        System.out.println("🚀 AI Sweep Triggered! Found " + pendingArticles.size() + " pending articles...");

        List<String> orderedHeadlines = new ArrayList<>();
        List<UpdateSentimentDto> updatesToSave = new ArrayList<>();

        System.out.println("🤖 Starting Sentiment Analysis (Qwen 2.5:3b)...");

        for (int i = 0; i < pendingArticles.size(); i++) {

            // Extract data from your custom DTO
            NewsForAiDto article = pendingArticles.get(i);
            String rawHeadline = article.getHeadline();

            String safeHeadline = (rawHeadline == null || rawHeadline.trim().isEmpty())
                    ? "عنوان غير متوفر"
                    : rawHeadline;

            orderedHeadlines.add(safeHeadline);

            // 1. Ask Qwen for Sentiment (Waits for answer)
            int score = sentimentService.analyzeSingleSentiment(safeHeadline);
            // 2. Ask Qwen for Credibility (Waits for answer)
            Float credibilityScore = sentimentService.analyzeCredibility(safeHeadline);

            // Build the Save DTO
            UpdateSentimentDto dto = new UpdateSentimentDto();
            dto.setId(article.getId());
            dto.setSentimentId(score);
            dto.setCredibilityScore(credibilityScore);
            updatesToSave.add(dto);

            String label = (score == 1) ? "POS" : (score == 3) ? "NEG" : "NEU";
            String shortHeadline = safeHeadline.length() > 40 ? safeHeadline.substring(0, 40) + "..." : safeHeadline;
            int percentage = (int) (credibilityScore * 100);
            System.out.printf("[%d/%d] ID: %d | %s | Trust: %d%% | %s%n",
                    (i + 1), pendingArticles.size(), article.getId(), label, percentage, shortHeadline);
        }

        System.out.println("🧠 Generating Vector Embeddings (Nomic v2) for all " + pendingArticles.size() + " articles...");
        double[][] embeddings = embeddingService.getEmbeddings(orderedHeadlines);

        // 3. The Fusion Loop
        for (int i = 0; i < updatesToSave.size(); i++) {
            double[] fusedVector = new double[769];

            if (embeddings != null && i < embeddings.length && embeddings[i] != null && embeddings[i].length >= 768) {
                System.arraycopy(embeddings[i], 0, fusedVector, 0, 768);
            } else {
                System.out.println("⚠️ Warning: Ollama returned a null embedding for ID " + updatesToSave.get(i).getId() + ". Saving as zeros.");
            }

            fusedVector[768] = updatesToSave.get(i).getSentimentId();
            updatesToSave.get(i).setEmbeddingVector(fusedVector);
        }

        // 4. Save everything back to the database
        newsRepository.batchUpdateStock(updatesToSave);

        long endTime = System.currentTimeMillis();
        double totalTimeInSeconds = (endTime - startTime) / 1000.0;

        System.out.println("✅ Successfully saved Sentiment and Vectors to MySQL!");
        System.out.println("⏱️ Total processing time: " + totalTimeInSeconds + " seconds.");
        System.out.println("---------------------------------------------------");
    }
}