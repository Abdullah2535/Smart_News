package com.smartnews.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartnews.backend.dtos.SearchResultDto;
import com.smartnews.backend.entities.News;
import com.smartnews.backend.repositories.NewsRepository;
import com.smartnews.backend.repositories.VectorOnlyProjection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@AllArgsConstructor
public class SearchService {

    private final NewsRepository newsRepository;
    private final EmbeddingService embeddingService;
    private final ObjectMapper objectMapper;

    public List<SearchResultDto> searchNews(String searchQuery,int limit) {

        System.out.println("🔍 Running HYBRID Search for: '" + searchQuery + "'");


        // --- 1. THE EXACT KEYWORD PASS (MySQL) ---
        List<Integer> exactMatchIds = newsRepository.findByHeadlineContainingIgnoreCase(searchQuery);

        // --- 2. THE SEMANTIC VECTOR PASS (Nomic AI) ---
        double[] searchVector = embeddingService.getEmbeddings(List.of(searchQuery))[0];
        List<VectorOnlyProjection> allVectors = newsRepository.findAllVectorsOnly();

        // This map will store: Article ID -> Cosine Similarity Score
        Map<Integer, Double> semanticScoreMap = new HashMap<>();

        for (VectorOnlyProjection item : allVectors) {
            try {
                double[] articleVector = objectMapper.readValue(item.getEmbeddingArray(), new TypeReference<double[]>() {});
                double similarity = calculateCosineSimilarity(searchVector, articleVector);

                if (similarity > 0.38) { // Only keep relevant math
                    semanticScoreMap.put(item.getId(), similarity);
                }
            } catch (JsonProcessingException e) {
                System.err.println("⚠️ Could not parse vector for ID: " + item.getId());
            }
        }

        // Sort the map by math score (highest first) and extract ONLY the Top 10 IDs
        List<Integer> semanticIds = semanticScoreMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit( limit)
                .map(Map.Entry::getKey)
                .toList();
        // --- 3. THE MERGE (Combining Both Brains) ---
        // A LinkedHashSet because it automatically removes duplicates AND keeps the order!
        Set<Integer> mergedIdsSet = new LinkedHashSet<>();

        // Add exact matches FIRST (they are guaranteed to be highly relevant)
        mergedIdsSet.addAll(exactMatchIds);

        // Add AI matches SECOND (to catch context/meaning)
        mergedIdsSet.addAll(semanticIds);

        // Turn the Set back into a List and apply the frontend Limit
        List<Integer> finalTopIds = mergedIdsSet.stream()
                .limit(limit)
                .toList();

        if (finalTopIds.isEmpty()) {
            return new ArrayList<>(); // No matches found
        }

        // --- STAGE 2: HYDRATION (Fetch full data for ONLY the winners) ---
        // findAllById is a built-in Spring Data JPA method!
        List<News> winningArticles = newsRepository.findAllById(finalTopIds);
        List<SearchResultDto> finalResults = new ArrayList<>();

        // We loop over 'topIds' to guarantee the final list stays in the correct sorted order
        for (Integer id : finalTopIds) {
            News fullArticle = winningArticles.stream()
                    .filter(news -> news.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (fullArticle != null) {
                SearchResultDto dto = new SearchResultDto();

                //  EVERYTHING for the frontend!
                dto.setId(fullArticle.getId());
                dto.setHeadline(fullArticle.getHeadline());
                dto.setThumbnail(fullArticle.getThumbnail()); // Useful for Angular!
                dto.setArticleUrl(fullArticle.getArticleUrl()); // Useful for Angular!

                dto.setSentimentId(fullArticle.getSentiment() != null ? fullArticle.getSentiment().getId() : 2);
                dto.setCredibilityScore(fullArticle.getCredibilityScore() != null ? fullArticle.getCredibilityScore() : 0.6f);

                dto.setSimilarityScore(semanticScoreMap.getOrDefault(id, 1.0));

                // The AI Fake News Flag
                dto.setCredibilityLabel(generateCredibilityLabel(dto.getCredibilityScore()));

                finalResults.add(dto);
            }
        }

        System.out.println("✅ Two-Stage Search Hydrated + Hybrid Search are completed! Returned " + finalResults.size() + " articles.");
        return finalResults;
    }

    // --- THE COSINE SIMILARITY FORMULA ---
    private double calculateCosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }

        if (normA == 0.0 || normB == 0.0) {
            return 0.0;
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
    private String generateCredibilityLabel(Float score) {
        if (score == null) return "لم يتم التقييم"; // Unrated

        if (score >= 0.9f) {
            return "حقيقة موضوعية"; // 1.0 (Pure Fact)
        } else if (score >= 0.7f) {
            return "موثوق غالباً"; // 0.8 (Mostly Reliable)
        } else if (score >= 0.5f) {
            return "محايد / غير مؤكد"; // 0.6 (Neutral)
        } else if (score >= 0.3f) {
            return "مبالغ فيه (إثارة)"; // 0.4 (Slightly Exaggerated)
        } else {
            return "تضليل / خادع (Clickbait)"; // 0.2 (Misleading)
        }
    }
}
