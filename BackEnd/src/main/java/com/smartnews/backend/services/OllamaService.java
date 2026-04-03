package com.smartnews.backend.services;

import com.smartnews.backend.dtos.OllamaGenerateRequest;
import com.smartnews.backend.dtos.OllamaGenerateResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OllamaService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private final String MODEL_NAME = "qwen2.5:3b";


    public List<Integer> analyzeSentimentBatch(List<String> headlines) {
        // Using parallelStream() here sends multiple requests to Ollama simultaneously,
        // making the process significantly faster than a standard for-loop
        return headlines.parallelStream()
                .map(this::analyzeSingleSentiment)
                .collect(Collectors.toList());
    }

    public int analyzeSingleSentiment(String headline) {
        String prompt = "أنت خبير في تحليل الأخبار. اقرأ هذا العنوان الإخباري: [" + headline + "]\n" +
                "حدد المشاعر بناءً على هذه القواعد الصارمة:\n" +
                "1 = إيجابي (انتصار، نمو، إنجاز)\n" +
                "2 = محايد (حقائق، اجتماعات روتينية)\n" +
                "3 = سلبي (حرب، موت، خسارة، جريمة)\n" +
                "أجب برقم واحد فقط (1 أو 2 أو 3). لا تكتب أي نص آخر.\n" +
                "النتيجة:";

        OllamaGenerateRequest.Options options = new OllamaGenerateRequest.Options(0.1, 2);
        OllamaGenerateRequest request = new OllamaGenerateRequest(MODEL_NAME, prompt, false, options);

        try {
            ResponseEntity<OllamaGenerateResponse> response = restTemplate.postForEntity(
                    OLLAMA_URL, request, OllamaGenerateResponse.class
            );

            if (response.getBody() != null && response.getBody().getResponse() != null) {
                String rawAnswer = response.getBody().getResponse();
                String justTheNumber = rawAnswer.replaceAll("\\D+", "");

                if (!justTheNumber.isEmpty()) {
                    int score = Integer.parseInt(justTheNumber);
                    if (score >= 1 && score <= 3) {
                        return score;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Sentiment API failed for headline: " + headline + " - " + e.getMessage());
        }

        return 2; // Default to Neutral if Ollama fails
    }

    public Float analyzeCredibility(String headline) {

        // The strict 1-to-5 prompt
        String prompt = "أنت خبير إعلامي. اقرأ هذا العنوان الإخباري: [" + headline + "]\n" +
                "قيّم صياغة هذا العنوان من 1 إلى 5 بناءً على هذه القواعد الصارمة:\n" +
                "5 = خبر حقيقي وطبيعي (تجاهل كلمات مثل 'عاجل' أو 'رسمياً' أو علامات التعجب، فهي عادية في الأخبار).\n" +
                "4 = خبر موثوق لكنه يحتوي على رأي أو تصريح لشخص.\n" +
                "3 = عنوان غامض لا يعطي معلومة واضحة.\n" +
                "2 = عنوان مثير للجدل أو مبالغ في صياغته.\n" +
                "1 = إعلان ترويجي أو خادع تماماً (Clickbait).\n" +
                "ملاحظة: معظم الأخبار السياسية والرياضية والاقتصادية يجب أن تأخذ 5 أو 4.\n" +
                "أجب برقم واحد فقط (من 1 إلى 5). لا تكتب أي نص آخر.\n" +
                "النتيجة:";

        OllamaGenerateRequest.Options options = new OllamaGenerateRequest.Options(0.1, 2);
        OllamaGenerateRequest request = new OllamaGenerateRequest(MODEL_NAME, prompt, false, options);

        try {
            ResponseEntity<OllamaGenerateResponse> response = restTemplate.postForEntity(
                    OLLAMA_URL, request, OllamaGenerateResponse.class
            );

            if (response.getBody() != null && response.getBody().getResponse() != null) {
                String rawAnswer = response.getBody().getResponse();
                String justTheNumber = rawAnswer.replaceAll("\\D+", "");

                if (!justTheNumber.isEmpty()) {
                    int score = Integer.parseInt(justTheNumber);
                    if (score >= 1 && score <= 5) {

                        // Converts 5 to 1.0, 4 to 0.8, 3 to 0.6, etc.
                        return score / 5.0f;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Credibility API failed for headline: " + headline + " - " + e.getMessage());
        }

        // Default to Neutral (60%) if Ollama fails or returns weird text
        return 0.6f;
    }
}
