package com.smartnews.backend.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class OllamaGenerateRequest {
    private String model;
    private String prompt;
    private boolean stream;
    private Options options; // NEW: The options block


    // Nested class for the hyperparameters
    @Setter
    @Getter
    public static class Options {
        private double temperature;
        private int num_predict;

        public Options(double temperature, int num_predict) {
            this.temperature = temperature;
            this.num_predict = num_predict;
        }

    }

}
