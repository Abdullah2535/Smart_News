package com.smartnews.backend.services;


import com.smartnews.backend.dtos.OllamaEmbedRequest;
import com.smartnews.backend.dtos.OllamaEmbedResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.List;


@Service
public class EmbeddingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String OLLAMA_URL = "http://localhost:11434/api/embed";
    private final String MODEL_NAME = "nomic-embed-text-v2-moe";

    public double[][] getEmbeddings(List<String> orderedHeadlines) {

        // Prepare the final 2D array to hold all the math
        double[][] finalEmbeddings = new double[orderedHeadlines.size()][768];

        for (int i = 0; i < orderedHeadlines.size(); i++) {
            String rawText = orderedHeadlines.get(i);

            // 1. CLEAN THE TEXT: Remove hidden newlines, tabs, and weird spaces
            String safeText = (rawText == null) ? "عنوان فارغ" : rawText.replaceAll("[\\r\\n\\t]+", " ").trim();

            // 2. SEND ONE AT A TIME: Prevents Ryzen RAM overload
            OllamaEmbedRequest request = new OllamaEmbedRequest(MODEL_NAME, List.of(safeText));

            try {
                ResponseEntity<OllamaEmbedResponse> response = restTemplate.postForEntity(
                        OLLAMA_URL, request, OllamaEmbedResponse.class
                );

                // Safely extract the single array and put it in the master list
                if (response.getBody() != null && response.getBody().getEmbeddings() != null && response.getBody().getEmbeddings().length > 0) {
                    finalEmbeddings[i] = response.getBody().getEmbeddings()[0];
                } else {
                    System.out.println("⚠️ Ollama returned empty array for: " + safeText);
                    finalEmbeddings[i] = new double[768]; // Fill with zeros to prevent crash
                }

            } catch (Exception e) {
                System.out.println("⚠️ Embedding failed for: " + safeText + " | Error: " + e.getMessage());
                finalEmbeddings[i] = new double[768];
            }
        }

        return finalEmbeddings;
    }
}
